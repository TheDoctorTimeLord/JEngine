package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.annotations.PackagesScan;
import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.classfinders.CompositeClassFinder;
import ru.jengine.beancontainer.classfinders.EmptyClassFinder;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultModuleFactory implements ModuleFactory {
    @Override
    public Module createAnnotatedModule(Class<?> moduleClass, ContainerConfiguration configuration) {
        ModuleContext context = createModuleContext(moduleClass, configuration);
        return createModule(moduleClass, context);
    }

    private ModuleContext createModuleContext(Class<?> moduleClass, ContainerConfiguration configuration) {
        ClassFinder classFinder = extractClassFinderFromModule(moduleClass, configuration);
        return new ModuleContext(classFinder, moduleClass);
    }

    @Override
    public ClassFinder extractClassFinderFromModule(Class<?> moduleClass, ContainerConfiguration configuration) {
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
        classFinder.scan(fixPackageToScan(packageToScan));
        return classFinder;
    }

    private static String fixPackageToScan(String packageToScan) {
        return packageToScan.endsWith(".") ? packageToScan : packageToScan + ".";
    }

    private Module createModule(Class<?> moduleClass, ModuleContext moduleContext) {
        try {
            Module module = ReflectionContainerUtils.createComponentWithDefaultConstructor(moduleClass);
            configureModule(module, moduleContext);
            return module;
        } catch (ContainerException e) {
            throw new ContainerException("Module [" + moduleClass + "] must have default constructor", e);
        } catch (ClassCastException e) {
            throw new ContainerException("Module [" + moduleClass + "] must implement interface Module", e);
        }
    }

    @Override
    public Module configureModule(Module module, ModuleContext moduleContext) {
        module.configure(moduleContext);
        return module;
    }
}
