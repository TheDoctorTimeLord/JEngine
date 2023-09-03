package ru.jengine.beancontainer.beanfactory;

public interface BeanFactory {
    Object buildBean(Class<?> beanClass);
    Object autowire(Object autowiredBean);
}
