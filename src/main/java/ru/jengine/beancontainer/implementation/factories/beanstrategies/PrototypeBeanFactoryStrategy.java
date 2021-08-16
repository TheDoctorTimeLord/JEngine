package ru.jengine.beancontainer.implementation.factories.beanstrategies;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.dataclasses.BeanContext;

public class PrototypeBeanFactoryStrategy implements BeanFactoryStrategy {
    private final Class<?> objectClass;

    public PrototypeBeanFactoryStrategy(Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public boolean needPrepare() {
        return false;
    }

    @Override
    public BeanContext getBean(BeanFactory factory) {
        return factory.buildBean(objectClass);
    }

    @Override
    public void clear() { }
}
