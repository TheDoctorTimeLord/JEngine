package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface BeanPostProcessor {
    void postProcess(BeanContext bean, ContainerContext context);
}
