package ru.jengine.beancontainer.implementation.factories;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.dataclasses.BeanContext;

public class SingletonBeanFactoryStrategy implements BeanFactoryStrategy {
    private final Class<?> objectClass;
    private BeanContext bean;

    public SingletonBeanFactoryStrategy(Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public void clear() {
        bean = null;
    }

    @Override
    public boolean needPrepare() {
        return true;
    }

    @Override
    public BeanContext getBean(BeanFactory factory) {
        if (bean == null) {
            synchronized (this) {
                if (bean == null) {
                    bean = factory.buildBean(objectClass);
                }
            }
        }
        return bean;
    }
}
