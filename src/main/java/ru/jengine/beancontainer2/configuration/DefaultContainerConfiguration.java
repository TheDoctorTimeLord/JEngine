package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.ContainerContextFactory;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.List;
import java.util.function.Supplier;

public class DefaultContainerConfiguration implements ContainerConfiguration {
    private final Class<?> mainModuleClass;
    private final Supplier<ClassFinder> classFinderFactory;
    private final ModuleFactory moduleFactory;
    private final ContextMetainfoFactory contextMetainfoFactory;
    private final ContainerContextFactory containerContextFactory;
    private final BeanCreationScopeResolver beanCreationScopeResolver;
    private final List<String> preloadedContextNames;

    DefaultContainerConfiguration(Class<?> mainModuleClass, Supplier<ClassFinder> classFinderFactory,
            ModuleFactory moduleFactory, ContextMetainfoFactory contextMetainfoFactory,
            ContainerContextFactory containerContextFactory, BeanCreationScopeResolver beanCreationScopeResolver,
            List<String> preloadedContextNames)
    {
        this.mainModuleClass = mainModuleClass;
        this.classFinderFactory = classFinderFactory;
        this.moduleFactory = moduleFactory;
        this.contextMetainfoFactory = contextMetainfoFactory;
        this.containerContextFactory = containerContextFactory;
        this.beanCreationScopeResolver = beanCreationScopeResolver;
        this.preloadedContextNames = preloadedContextNames;
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

    @Override
    public List<String> getPreloadedContextNames() {
        return preloadedContextNames;
    }

    @Override
    public BeanCreationScopeResolver getBeanCreationScopeResolver() {
        return beanCreationScopeResolver;
    }
}
