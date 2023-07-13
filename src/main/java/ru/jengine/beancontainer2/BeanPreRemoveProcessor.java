package ru.jengine.beancontainer2;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
