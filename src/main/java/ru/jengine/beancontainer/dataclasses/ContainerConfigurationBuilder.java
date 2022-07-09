package ru.jengine.beancontainer.dataclasses;

import java.util.List;
import java.util.function.Supplier;

import ru.jengine.beancontainer.ClassFinder;

public interface ContainerConfigurationBuilder {
    ContainerConfigurationBuilder setAdditionalBeans(List<Object> additionalBeans);
    ContainerConfigurationBuilder addAdditionalBean(Object bean);
    ContainerConfigurationBuilder setClassFinderConsumer(Supplier<ClassFinder> classFinderConsumer);
    ContainerConfiguration build();
}
