package ru.jengine.beancontainer.dataclasses;

public class BeanContext {
    private Object bean;
    private boolean willDropped;

    public BeanContext() {}

    public BeanContext(Object bean) {
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
    }

    public void setInstance(Object bean) {
        this.bean = bean;
    }

    public boolean isWillDropped() {
        return willDropped;
    }

    public void setWillDropped(boolean willDropped) {
        this.willDropped = willDropped;
    }
}
