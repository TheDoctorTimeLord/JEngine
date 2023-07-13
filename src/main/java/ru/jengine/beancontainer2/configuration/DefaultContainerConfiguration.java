package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.function.Supplier;

public class DefaultContainerConfiguration implements ContainerConfiguration {
    private final Class<?> mainModuleClass;
    private final Supplier<ClassFinder> classFinderFactory;
    private final ModuleFactory moduleFactory;

    DefaultContainerConfiguration(Class<?> mainModuleClass, Supplier<ClassFinder> classFinderFactory,
            ModuleFactory moduleFactory) {
        this.mainModuleClass = mainModuleClass;
        this.classFinderFactory = classFinderFactory;
        this.moduleFactory = moduleFactory;
    }

    @Override
    public Class<?> getMainModuleClass() {
        return mainModuleClass;
    }

    @Override
    public Supplier<ClassFinder> getClassFinderFactory() {
        return classFinderFactory;
    }

    @Override
    public ModuleFactory getModuleFactory() {
        return moduleFactory;
    }
}
