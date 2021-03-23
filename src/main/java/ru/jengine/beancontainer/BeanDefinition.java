package ru.jengine.beancontainer;

public interface BeanDefinition {
    Class<?> getBeanClass();
    boolean isSingleton();
}
