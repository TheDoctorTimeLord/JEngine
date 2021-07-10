package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface BeanFactoryStrategy {
    boolean needPrepare();
    BeanContext getBean(BeanFactory factory);
    void clear();
}
