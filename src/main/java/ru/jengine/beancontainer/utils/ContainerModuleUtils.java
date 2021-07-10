package ru.jengine.beancontainer.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFinder;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.implementation.classfinders.ClassPathScanner;
import ru.jengine.beancontainer.implementation.classfinders.CompositeClassFinder;
import ru.jengine.beancontainer.implementation.classfinders.EmptyClassFinder;
import ru.jengine.beancontainer.service.Constants;

public class ContainerModuleUtils {
    public static List<Module> getAllSubmodules(Module mainModule) {
        List<Module> result = new ArrayList<>();
        Set<Class<?>> moduleClasses = new HashSet<>();
        Queue<Module> notHandledModules = new LinkedList<>();

        notHandledModules.add(mainModule);
        moduleClasses.add(mainModule.getClass());

        while (!notHandledModules.isEmpty()) {
            Module module = notHandledModules.remove();

            List<Class<?>> submodules = module.getSubmodules().stream()
                    .filter(cls -> !moduleClasses.contains(cls))
                    .collect(Collectors.toList());
            moduleClasses.addAll(submodules);
            submodules.stream()
                    .map(ContainerModuleUtils::createModule)
                    .forEach(submodule -> {
                        notHandledModules.add(submodule);
                        result.add(submodule);
                    });
        }

        return result;
    }

    private static ModuleContext createModuleContext(Class<?> moduleClass) {
        ClassFinder classFinder = extractClassFinderFromModule(moduleClass);
        return new ModuleContext(classFinder, moduleClass);
    }

    private static ClassFinder extractClassFinderFromModule(Class<?> moduleClass) {
        if (moduleClass.isAnnotationPresent(PackageScan.class)) { //TODO исправить на AnnotationUtils
            String packageToScan = moduleClass.getAnnotation(PackageScan.class).value();
            return scanPackage(packageToScan);
        } else if (moduleClass.isAnnotationPresent(PackagesScan.class)) {
            return new CompositeClassFinder(Stream.of(moduleClass.getAnnotation(PackagesScan.class).value())
                    .map(packageScan -> scanPackage(packageScan.value()))
                    .collect(Collectors.toList()));
        }
        return new EmptyClassFinder();
    }

    private static ClassFinder scanPackage(String packageToScan) { //TODO в дальнейшем вынести в конфигурации контейнера
        ClassFinder classFinder = new ClassPathScanner();
        classFinder.scan(packageToScan);
        return classFinder;
    }

    private static Module createModule(Class<?> moduleClass, ModuleContext moduleContext) {
        Module module;
        try {
            module = (Module) BeanUtils.createObjectWithDefaultConstructor(moduleClass);
        } catch (ContainerException e) {
            throw new ContainerException("Module [" + moduleClass + "] must have default constructor", e);
        } catch (ClassCastException e) {
            throw new ContainerException("Module [" + moduleClass + "] must implement interface Module", e);
        }

        module.configure(moduleContext);
        return module;
    }

    public static Module createModule(Class<?> moduleClass) {
        ModuleContext context = createModuleContext(moduleClass);
        return createModule(moduleClass, context);
    }

    public static Set<Class<?>> getAllModuleFinders(List<Module> foundedModules) {
        return foundedModules.stream()
                .flatMap(module -> module.getImplementations(ModuleFinder.class).stream())
                .filter(ClassUtils.IS_CLASS_PREDICATE)
                .filter(cls -> cls.isAnnotationPresent(ModuleFinderMarker.class))
                .collect(Collectors.toSet());
    }

    public static List<BeanDefinition> extractAllBeanDefinition(List<Module> modules) {
        return modules.stream()
                .flatMap(module -> module.getBeanDefinitionReaders().stream()
                        .flatMap(reader -> reader.readBeanDefinitions().stream())
                )
                .collect(Collectors.toList());
    }

    public static String extractContextForModule(Module module) {
        Context annotation = module.getClass().getAnnotation(Context.class);
        if (annotation == null) {
            return Constants.DEFAULT_CONTEXT;
        }
        return annotation.value();
    }
}
