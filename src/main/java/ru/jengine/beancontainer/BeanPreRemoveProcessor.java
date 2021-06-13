package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(BeanContext bean, ContainerContext context);
}
