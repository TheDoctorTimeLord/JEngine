package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.classfinders.ClassPathScanner;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.ContainerContextFactory;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.extentions.defaultconf.DefaultBeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.defaultconf.DefaultContainerContextFactory;
import ru.jengine.beancontainer2.extentions.defaultconf.DefaultContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.DefaultModuleFactory;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.List;
import java.util.function.Supplier;

public class DefaultContainerConfigurationBuilder {
    private final Class<?> mainModuleClass;
    private Supplier<ClassFinder> classFinderFactory = ClassPathScanner::new;
    private ModuleFactory moduleFactory = new DefaultModuleFactory();
    private ContextMetainfoFactory contextMetainfoFactory = new DefaultContextMetainfoFactory();
    private ContainerContextFactory containerContextFactory = new DefaultContainerContextFactory();
    private BeanCreationScopeResolver beanCreationScopeResolver = new DefaultBeanCreationScopeResolver();
    private List<String> preloadedContextNames = List.of(
            Constants.Contexts.INFRASTRUCTURE_CONTEXT
    );

    DefaultContainerConfigurationBuilder(Class<?> mainModuleClass) {
        this.mainModuleClass = mainModuleClass;
    }

    public DefaultContainerConfigurationBuilder classFinderFactory(Supplier<ClassFinder> classFinderFactory) {
        this.classFinderFactory = classFinderFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder moduleFactory(ModuleFactory moduleFactory) {
        this.moduleFactory = moduleFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder contextMetainfoFactory(ContextMetainfoFactory contextMetainfoFactory) {
        this.contextMetainfoFactory = contextMetainfoFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder containerContextFactory(ContainerContextFactory containerContextFactory) {
        this.containerContextFactory = containerContextFactory;
        return this;
    }

    public void beanCreationScopeResolver(BeanCreationScopeResolver beanCreationScopeResolver) {
        this.beanCreationScopeResolver = beanCreationScopeResolver;
    }

    public DefaultContainerConfigurationBuilder preloadedContextNames(List<String> preloadedContextNames) {
        this.preloadedContextNames = preloadedContextNames;
        return this;
    }

    public ContainerConfiguration build() {
        return new DefaultContainerConfiguration(mainModuleClass, classFinderFactory, moduleFactory,
                contextMetainfoFactory, containerContextFactory, beanCreationScopeResolver, preloadedContextNames);
    }
}
