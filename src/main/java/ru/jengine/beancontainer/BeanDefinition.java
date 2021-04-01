package ru.jengine.beancontainer;

public interface BeanDefinition extends ConfigurableByPreProcessor {
    Class<?> getBeanClass();
    boolean isSingleton();
}
