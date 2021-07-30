package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface ConfigurableBeanFactory {
    void configure(ContainerContext infrastructureContext);
    void beforeRemove(BeanContext beanContext);
}
