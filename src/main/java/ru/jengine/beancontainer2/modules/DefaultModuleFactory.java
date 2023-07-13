package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.annotations.PackageScan;
import ru.jengine.beancontainer2.annotations.PackagesScan;
import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.classfinders.CompositeClassFinder;
import ru.jengine.beancontainer2.classfinders.EmptyClassFinder;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.utils.AnnotationUtils;
import ru.jengine.beancontainer2.utils.ReflectionContainerUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultModuleFactory implements ModuleFactory {
    @Override
    public Module createModule(Class<?> moduleClass, ContainerConfiguration configuration) {
        ModuleContext context = createModuleContext(moduleClass, configuration);
        return createModule(moduleClass, context);
    }

    private static ModuleContext createModuleContext(Class<?> moduleClass, ContainerConfiguration configuration) {
        ClassFinder classFinder = extractClassFinderFromModule(moduleClass, configuration);
        return new ModuleContext(classFinder, moduleClass);
    }

    private static ClassFinder extractClassFinderFromModule(Class<?> moduleClass, ContainerConfiguration configuration) {
        if (AnnotationUtils.isAnnotationPresent(moduleClass, PackageScan.class)) {
            String packageToScan = AnnotationUtils.getAnnotation(moduleClass, PackageScan.class).value();
            return scanPackage(packageToScan, configuration);
        } else if (AnnotationUtils.isAnnotationPresent(moduleClass, PackagesScan.class)) {
            return new CompositeClassFinder(Stream.of(AnnotationUtils.getAnnotation(moduleClass, PackagesScan.class).value())
                    .map(packageScan -> scanPackage(packageScan.value(), configuration))
                    .collect(Collectors.toList()));
        }
        return EmptyClassFinder.INSTANCE;
    }

    private static ClassFinder scanPackage(String packageToScan, ContainerConfiguration configuration) {
        ClassFinder classFinder = configuration.getClassFinderFactory().get();
        classFinder.scan(packageToScan);
        return classFinder;
    }

    private static Module createModule(Class<?> moduleClass, ModuleContext moduleContext) {
        try {
            Module module = (Module) ReflectionContainerUtils.createObjectWithDefaultConstructor(moduleClass);
            module.configure(moduleContext);
            return module;
        } catch (ContainerException e) {
            throw new ContainerException("Module [" + moduleClass + "] must have default constructor", e);
        } catch (ClassCastException e) {
            throw new ContainerException("Module [" + moduleClass + "] must implement interface Module", e);
        }
    }
}
