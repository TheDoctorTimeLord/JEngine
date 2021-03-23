package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface BeanFactory {
    BeanContext buildBean(Class<?> beanClass);
}
