package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface ConfigurableBeanFactory {
    void beforeRemove(BeanContext beanContext);
}
