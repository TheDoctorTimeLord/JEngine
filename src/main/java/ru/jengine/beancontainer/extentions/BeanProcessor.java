package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.containercontext.ContainerContext;

public interface BeanProcessor {
    void preConstructProcess(BeanDefinition beanDefinition, ContainerContext context);
    Object constructProcess(Object bean, Class<?> beanClass, ContainerContext context);
    void postConstructProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
