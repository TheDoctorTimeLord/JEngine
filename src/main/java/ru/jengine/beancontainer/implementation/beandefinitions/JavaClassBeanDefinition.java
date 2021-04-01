package ru.jengine.beancontainer.implementation.beandefinitions;

import ru.jengine.beancontainer.BeanDefinition;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final boolean isSingleton;
    private boolean mustRemovedAfterPreProcess = false;

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

    @Override
    public boolean mustRemovedAfterPreProcess() {
        return mustRemovedAfterPreProcess;
    }

    @Override
    public void setMustRemovedAfterPreProcess(boolean flag) {
        this.mustRemovedAfterPreProcess = flag;
    }
}
