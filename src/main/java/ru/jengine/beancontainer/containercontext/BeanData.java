package ru.jengine.beancontainer.containercontext;

public class BeanData {
    protected final Object beanValue;
    protected final Class<?> beanBaseClass;

    public BeanData(Object beanValue, Class<?> beanBaseClass) {
        this.beanValue = beanValue;
        this.beanBaseClass = beanBaseClass;
    }

    public Object getBeanValue() {
        return beanValue;
    }

    public Class<?> getBeanBaseClass() {
        return beanBaseClass;
    }
}
