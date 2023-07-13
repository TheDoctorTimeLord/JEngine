package ru.jengine.beancontainer2;

public interface BeanProcessor {
    void preConstructProcess(BeanDefinition beanDefinition, ContainerContext context);
    Object postConstructProcess(Object bean, Class<?> beanClass, ContainerContext context);
    void initializeBeanProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
