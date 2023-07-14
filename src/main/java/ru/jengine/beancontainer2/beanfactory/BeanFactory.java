package ru.jengine.beancontainer2.beanfactory;

public interface BeanFactory {
    Object buildBean(Class<?> beanClass);
    Object autowire(Object autowiredBean);
}
