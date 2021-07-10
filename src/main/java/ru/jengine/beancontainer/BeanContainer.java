package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;

public interface BeanContainer {
    void initialize(ContainerConfiguration configuration);
    void stop();

    <T> T getBean(Class<?> beanClass);

    void registerContext(String name, ContainerContext context);
    void reloadContext(String name);
    void removeContext(String name);
}
