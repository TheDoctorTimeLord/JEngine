package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.BeanDefinition;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final boolean isSingleton;

    public JavaClassBeanDefinition(Class<?> beanClass, boolean isSingleton) {
        this.beanClass = beanClass;
        this.isSingleton = isSingleton;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public boolean isSingleton() {
        return isSingleton;
    }
}
