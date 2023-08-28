package ru.jengine.beancontainer2.beandefinitions;

import com.google.common.base.Objects;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final String scopeName;
    private final Supplier<Object> beanProducer;

    public JavaClassBeanDefinition(Class<?> beanClass, String scopeName, @Nullable Supplier<Object> beanProducer) {
        this.beanClass = beanClass;
        this.scopeName = scopeName;
        this.beanProducer = beanProducer;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getScopeName() {
        return scopeName;
    }

    @Nullable
    @Override
    public Supplier<Object> getBeanProducer() {
        return beanProducer;
    }

    @Override
    public String toString() {
        return "beanClass=" + beanClass.getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JavaClassBeanDefinition that = (JavaClassBeanDefinition)o;
        return Objects.equal(beanClass, that.beanClass);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(beanClass);
    }
}
