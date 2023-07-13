package ru.jengine.beancontainer.beandefinitions;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;
    private final String scopeName;
    private final Supplier<Object> beanProducer;

    public JavaClassBeanDefinition(Class<?> beanClass, String scopeName) {
        this(beanClass, scopeName, null);
    }

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
        return "{beanClass=" + beanClass +
                ", scopeName='" + scopeName + '\'' +
                ", withProducer=" + (beanProducer != null) +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        JavaClassBeanDefinition that = (JavaClassBeanDefinition) object;
        return Objects.equals(beanClass, that.beanClass) && Objects.equals(scopeName, that.scopeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanClass, scopeName);
    }
}
