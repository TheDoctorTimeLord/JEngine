package ru.jengine.beancontainer.implementation.factories;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.dataclasses.BeanContext;

public class ExistBeanFactoryStrategy implements BeanFactoryStrategy {
    private final BeanContext bean;

    public ExistBeanFactoryStrategy(Object bean) {
        this.bean = new BeanContext(bean);
    }

    @Override
    public boolean needPrepare() {
        return false;
    }

    @Override
    public BeanContext getBean(BeanFactory factory) {
        return bean;
    }

    @Override
    public void clear() { }
}
