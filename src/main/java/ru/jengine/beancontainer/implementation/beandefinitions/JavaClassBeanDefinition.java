package ru.jengine.beancontainer.implementation.beandefinitions;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanFactoryStrategy;

import com.google.common.base.Objects;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final BeanFactoryStrategy beanFactoryStrategy;
    private boolean mustRemovedAfterPreProcess = false;

    public JavaClassBeanDefinition(Class<?> beanClass, BeanFactoryStrategy beanFactoryStrategy) {
        this.beanClass = beanClass;
        this.beanFactoryStrategy = beanFactoryStrategy;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public BeanFactoryStrategy getBeanFactoryStrategy() {
        return beanFactoryStrategy;
    }

    @Override
    public boolean mustRemovedAfterPreProcess() {
        return mustRemovedAfterPreProcess;
    }

    @Override
    public void setMustRemovedAfterPreProcess(boolean flag) {
        this.mustRemovedAfterPreProcess = flag;
    }

    @Override
    public String toString() {
        return "beanClass=" + beanClass.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JavaClassBeanDefinition that = (JavaClassBeanDefinition)o;
        return Objects.equal(beanClass, that.beanClass);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(beanClass);
    }
}
