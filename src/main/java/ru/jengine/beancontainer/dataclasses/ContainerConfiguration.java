package ru.jengine.beancontainer.dataclasses;

import java.util.List;
import java.util.function.Supplier;

import ru.jengine.beancontainer.ClassFinder;

public interface ContainerConfiguration {
    List<Object> getAdditionalBeans();
    Supplier<ClassFinder> getClassFinderConsumer();
    Class<?> getMainModuleClass();

    static ContainerConfigurationBuilder builder(Class<?> mainModule) {
        return new ContainerConfigurationImpl(mainModule);
    }

    static ContainerConfiguration build(Class<?> mainModule) {
        return builder(mainModule).build();
    }
}
