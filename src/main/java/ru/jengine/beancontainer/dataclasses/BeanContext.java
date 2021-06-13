package ru.jengine.beancontainer.dataclasses;

import java.util.Collection;

public class BeanContext {
    private Class<?> beanClass;
    private Object bean;

    public BeanContext(Class<?> beanClass) {
        this(null, beanClass);
    }

    public BeanContext(Object bean) {
        this(bean, bean.getClass());
    }

    public BeanContext(Object bean, Class<?> beanClass) {
        this.bean = bean;
        this.beanClass = beanClass;
    }

    public <T> T getBean() {
        return (T) bean;
    }

    public void setInstance(Object bean) {
        this.bean = bean;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public boolean isCollectionBean() {
        return bean != null && bean instanceof Collection;
    }
}
