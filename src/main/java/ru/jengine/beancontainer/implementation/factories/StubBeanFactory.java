package ru.jengine.beancontainer.implementation.factories;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.dataclasses.BeanContext;

public class StubBeanFactory implements BeanFactory {
    @Override
    public BeanContext buildBean(Class<?> beanClass) {
        return null;
    }
}
