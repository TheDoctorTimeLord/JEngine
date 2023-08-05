package ru.jengine.beancontainer2.beandefinitions;

import java.util.function.Supplier;

public interface BeanDefinition {
    Class<?> getBeanClass();
    String getScopeName();

    Supplier<Object> getBeanProducer();
}
