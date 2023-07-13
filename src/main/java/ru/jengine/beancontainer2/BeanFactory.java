package ru.jengine.beancontainer2;

public interface BeanFactory {
    Object buildBean(Class<?> beanClass);
}
