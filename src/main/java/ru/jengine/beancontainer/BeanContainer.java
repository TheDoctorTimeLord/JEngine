package ru.jengine.beancontainer;

public interface BeanContainer {
    void initialize(Class<?> mainModule, Object... additionalBeans);
}
