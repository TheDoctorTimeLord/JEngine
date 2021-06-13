package ru.jengine.beancontainer;

public interface BeanContainer {
    void initialize(Class<?> mainModule, Object... additionalBeans);
    void stop();

    <T> T getBean(Class<?> beanClass);

    void registerContext(String name, ContainerContext context);
    void reloadContext(String name);
    void removeContext(String name);
}
