package ru.jengine.beancontainer.operations.defaultimpl;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.modulefinders.ModuleFinder;
import ru.jengine.beancontainer.modulefinders.SyntheticModuleFinder;
import ru.jengine.beancontainer.modules.Module;
import ru.jengine.beancontainer.modules.ModuleContext;
import ru.jengine.beancontainer.modules.ModuleFactory;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;
import ru.jengine.beancontainer.operations.ResultConstants;
import ru.jengine.beancontainer.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;
import ru.jengine.utils.CollectionUtils;
import ru.jengine.utils.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleFinderOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult operationResult, ContainerState state) {
        ContainerConfiguration configuration = state.getContainerConfiguration();
        SyntheticModuleFinder syntheticModuleFinder = new SyntheticModuleFinder();

        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_STANDARD_TOOLS);
        syntheticModuleFinder.addModuleClass(configuration.getMainModuleClass());

        List<Module> foundedModules = findAllModules(syntheticModuleFinder, configuration);
        Map<String, List<Module>> modulesByContext = CollectionUtils.groupBy(foundedModules, Module::getContextName);

        addExternalSetModules(modulesByContext, configuration);

        operationResult.putResult(ResultConstants.MODULES_BY_CONTEXT, modulesByContext);
    }

    private static List<Module> findAllModules(ModuleFinder mainModuleFinder, ContainerConfiguration configuration) {
        Queue<ModuleFinder> moduleFinders = new ArrayDeque<>();
        List<Module> result = new ArrayList<>();
        Set<Class<?>> foundedModules = new HashSet<>();

        moduleFinders.add(mainModuleFinder);

        while (!moduleFinders.isEmpty()) {
            ModuleFinder moduleFinder = moduleFinders.poll();
            Queue<Module> mainModules = new ArrayDeque<>(moduleFinder.find(configuration));

            while (!mainModules.isEmpty()) {
                Module masterModule = mainModules.poll();

                List<Module> uniqueModules = getUniqueModules(masterModule, foundedModules, configuration);

                moduleFinders.addAll(getAllModuleFinders(uniqueModules));
                result.addAll(uniqueModules);
                uniqueModules.stream()
                        .map(Object::getClass)
                        .forEach(foundedModules::add);
            }
        }

        return result;
    }

    private static List<Module> getUniqueModules(Module masterModule, Set<Class<?>> foundedModels,
            ContainerConfiguration configuration)
    {
        List<Module> submodules = getAllSubmodules(masterModule, configuration);
        return Stream.concat(Stream.of(masterModule), submodules.stream())
                .filter(module -> !foundedModels.contains(module.getClass()))
                .collect(Collectors.toList());
    }

    private static List<Module> getAllSubmodules(Module masterModule, ContainerConfiguration configuration) {
        List<Module> result = new ArrayList<>();

        ModuleFactory moduleFactory = configuration.getModuleFactory();
        Set<Class<?>> moduleClasses = new HashSet<>();
        Queue<Module> notHandledModules = new ArrayDeque<>();

        notHandledModules.add(masterModule);
        moduleClasses.add(masterModule.getClass());

        while (!notHandledModules.isEmpty()) {
            Module module = notHandledModules.poll();

            List<Class<?>> submodules = module.getSubmodules().stream()
                    .filter(cls -> !moduleClasses.contains(cls))
                    .toList();
            moduleClasses.addAll(submodules);
            submodules.stream()
                    .map(moduleClass -> moduleFactory.createAnnotatedModule(moduleClass, configuration))
                    .forEach(submodule -> {
                        notHandledModules.add(submodule);
                        result.add(submodule);
                    });
        }

        return result;
    }

    private static List<ModuleFinder> getAllModuleFinders(List<Module> uniqueModules) {
        Set<Class<?>> moduleFinderClasses = extractAllModuleFinderClasses(uniqueModules);

        return moduleFinderClasses.stream()
                .map(cls -> {
                    try {
                        return (ModuleFinder) ReflectionContainerUtils.createObjectWithDefaultConstructor(cls);
                    } catch (ClassCastException e) {
                        throw new ContainerException("Class [%s] is not implemented ModuleFinder".formatted(cls), e);
                    } catch (ContainerException e) {
                        throw new ContainerException(("Exception during creating ModuleFinder [%s]. WARNING! " +
                                "ModuleFinder must have default constructor").formatted(cls), e);
                    } catch (Exception e) {
                        throw new ContainerException("Exception during creating ModuleFinder [%s]".formatted(cls), e);
                    }
                })
                .toList();
    }

    private static Set<Class<?>> extractAllModuleFinderClasses(List<Module> foundedModules) {
        return foundedModules.stream()
                .flatMap(module -> module.getImplementations(ModuleFinder.class).stream())
                .filter(ReflectionUtils.IS_CLASS_PREDICATE)
                .filter(cls -> AnnotationUtils.isAnnotationPresent(cls, ModuleFinderMarker.class))
                .collect(Collectors.toSet());
    }

    protected void addExternalSetModules(Map<String, List<Module>> modules, ContainerConfiguration configuration) {
        Map<String, List<Module>> externalSetModules = configuration.getExternalSetModules();
        ModuleFactory moduleFactory = configuration.getModuleFactory();

        externalSetModules.values().stream()
                .flatMap(Collection::stream)
                .forEach(module -> moduleFactory.configureModule(module, new ModuleContext(
                        configuration.getClassFinderFactory().get(),
                        module.getClass()
                )));

        for (Map.Entry<String, List<Module>> entry : externalSetModules.entrySet()) {
            modules.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
        }
    }
}
