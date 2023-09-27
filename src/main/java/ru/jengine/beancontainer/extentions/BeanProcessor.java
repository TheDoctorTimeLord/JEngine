package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.containercontext.ContainerContext;

public interface BeanProcessor {
    void preConstructProcess(BeanDefinition beanDefinition, ContainerContext context);
    Object constructProcess(BeanData bean, ContainerContext context);
    void postConstructProcess(BeanData bean, ContainerContext context);
}
