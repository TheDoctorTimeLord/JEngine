package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.classfinders.ClassPathScanner;
import ru.jengine.beancontainer2.modules.DefaultModuleFactory;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.function.Supplier;

public class DefaultContainerConfigurationBuilder {
    private final Class<?> mainModuleClass;
    private Supplier<ClassFinder> classFinderFactory = ClassPathScanner::new;
    private ModuleFactory moduleFactory = new DefaultModuleFactory();

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

    public ContainerConfiguration build() {
        return new DefaultContainerConfiguration(mainModuleClass, classFinderFactory, moduleFactory);
    }
}
