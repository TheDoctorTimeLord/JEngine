package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.function.Supplier;

public interface ContainerConfiguration {
    Class<?> getMainModuleClass();
    Supplier<ClassFinder> getClassFinderFactory();
    ModuleFactory getModuleFactory();

    static DefaultContainerConfigurationBuilder builder(Class<?> mainModuleClass) {
        return new DefaultContainerConfigurationBuilder(mainModuleClass);
    }
}
