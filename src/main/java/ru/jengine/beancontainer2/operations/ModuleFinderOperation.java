package ru.jengine.beancontainer2.operations;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer2.utils.AnnotationUtils;
import ru.jengine.beancontainer2.modules.ModuleFactory;
import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.modulefinders.ModuleFinder;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.modulefinders.SyntheticModuleFinder;
import ru.jengine.beancontainer2.utils.ReflectionContainerUtils;
import ru.jengine.utils.CollectionUtils;
import ru.jengine.utils.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleFinderOperation implements ContainerOperation<EmptyOperationResult> {
    @Override
    public OperationResult apply(EmptyOperationResult emptyResult, ContainerState context) {
        ContainerConfiguration configuration = context.getContainerConfiguration();
        SyntheticModuleFinder syntheticModuleFinder = new SyntheticModuleFinder();

        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_MAIN_INFRASTRUCTURE_MODULE);
        syntheticModuleFinder.addModuleClass(Constants.BEAN_CONTAINER_MAIN_MODULE);
        syntheticModuleFinder.addModuleClass(configuration.getMainModuleClass());

        List<Module> foundedModules = findAllModules(syntheticModuleFinder, configuration);
        Map<String, List<Module>> modulesByContext = CollectionUtils.groupBy(foundedModules, Module::getContextName);

        return new OperationResultWithModules(modulesByContext);
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

    private static List<Module> getAllSubmodules(Module mainModule, ContainerConfiguration configuration) {
        List<Module> result = new ArrayList<>();

        ModuleFactory moduleFactory = configuration.getModuleFactory();
        Set<Class<?>> moduleClasses = new HashSet<>();
        Queue<Module> notHandledModules = new ArrayDeque<>();

        notHandledModules.add(mainModule);
        moduleClasses.add(mainModule.getClass());

        while (!notHandledModules.isEmpty()) {
            Module module = notHandledModules.poll();

            List<Class<?>> submodules = module.getSubmodules().stream()
                    .filter(cls -> !moduleClasses.contains(cls))
                    .toList();
            moduleClasses.addAll(submodules);
            submodules.stream()
                    .map(moduleClass -> moduleFactory.createModule(moduleClass, configuration))
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
                .map(cls -> (ModuleFinder)ReflectionContainerUtils.createObjectWithDefaultConstructor(cls))
                .toList();
    }

    private static Set<Class<?>> extractAllModuleFinderClasses(List<Module> foundedModules) {
        return foundedModules.stream()
                .flatMap(module -> module.getImplementations(ModuleFinder.class).stream())
                .filter(ReflectionUtils.IS_CLASS_PREDICATE)
                .filter(cls -> AnnotationUtils.isAnnotationPresent(cls, ModuleFinderMarker.class))
                .collect(Collectors.toSet());
    }

    public record OperationResultWithModules(Map<String, List<Module>> modulesByContext) implements OperationResult { }
}
