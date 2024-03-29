package ru.jengine.beancontainer.configuration;

import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.ContainerContextFactory;
import ru.jengine.beancontainer.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer.modules.ModuleFactory;
import ru.jengine.beancontainer.modules.Module;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultContainerConfiguration implements ContainerConfiguration {
    private final Map<String, List<Module>> externalSetModules;
    private final Class<?> mainModuleClass;
    private final Supplier<ClassFinder> classFinderFactory;
    private final ModuleFactory moduleFactory;
    private final ContextMetainfoFactory contextMetainfoFactory;
    private final ContainerContextFactory containerContextFactory;
    private final BeanCreationScopeResolver beanCreationScopeResolver;
    private final List<String> preloadedContextNames;

    DefaultContainerConfiguration(Map<String, List<Module>> externalSetModules, Class<?> mainModuleClass,
            Supplier<ClassFinder> classFinderFactory, ModuleFactory moduleFactory,
            ContextMetainfoFactory contextMetainfoFactory, ContainerContextFactory containerContextFactory,
            BeanCreationScopeResolver beanCreationScopeResolver, List<String> preloadedContextNames)
    {
        this.externalSetModules = externalSetModules;
        this.mainModuleClass = mainModuleClass;
        this.classFinderFactory = classFinderFactory;
        this.moduleFactory = moduleFactory;
        this.contextMetainfoFactory = contextMetainfoFactory;
        this.containerContextFactory = containerContextFactory;
        this.beanCreationScopeResolver = beanCreationScopeResolver;
        this.preloadedContextNames = preloadedContextNames;
    }

    @Override
    public Map<String, List<Module>> getExternalSetModules() {
        return externalSetModules;
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
