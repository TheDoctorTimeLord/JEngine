package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.extentions.ContainerContextFactory;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.function.Supplier;

public class DefaultContainerConfiguration implements ContainerConfiguration {
    private final Class<?> mainModuleClass;
    private final Supplier<ClassFinder> classFinderFactory;
    private final ModuleFactory moduleFactory;
    private final ContextMetainfoFactory contextMetainfoFactory;
    private final ContainerContextFactory containerContextFactory;

    DefaultContainerConfiguration(Class<?> mainModuleClass, Supplier<ClassFinder> classFinderFactory,
            ModuleFactory moduleFactory, ContextMetainfoFactory contextMetainfoFactory,
            ContainerContextFactory containerContextFactory) {
        this.mainModuleClass = mainModuleClass;
        this.classFinderFactory = classFinderFactory;
        this.moduleFactory = moduleFactory;
        this.contextMetainfoFactory = contextMetainfoFactory;
        this.containerContextFactory = containerContextFactory;
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

    @Override
    public ContextMetainfoFactory getContextMetainfoFactory() {
        return contextMetainfoFactory;
    }

    @Override
    public ContainerContextFactory getContainerContextFactory() {
        return containerContextFactory;
    }
}
