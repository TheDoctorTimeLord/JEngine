package ru.jengine.beancontainer.dataclasses;

public class BeanHolder {
    private final Class<?> beanClass;
    private Object bean;

    public BeanHolder(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public boolean wasInitialized() {
        return bean != null;
    }
}
