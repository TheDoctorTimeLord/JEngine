package ru.jengine.beancontainer.beandefinitions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface BeanDefinition {
    Class<?> getBeanClass();
    String getScopeName();

    @Nullable
    Supplier<Object> getBeanProducer();
}
