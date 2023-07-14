package ru.jengine.beancontainer2.extentions;

import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.containercontext.ContainerContext;

public interface BeanProcessor {
    void preConstructProcess(BeanDefinition beanDefinition, ContainerContext context);
    Object constructProcess(Object bean, Class<?> beanClass, ContainerContext context);
    void postConstructProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
