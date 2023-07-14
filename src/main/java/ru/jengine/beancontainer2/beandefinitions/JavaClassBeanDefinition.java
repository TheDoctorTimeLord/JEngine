package ru.jengine.beancontainer2.beandefinitions;

import com.google.common.base.Objects;

public class JavaClassBeanDefinition implements BeanDefinition {
    private final Class<?> beanClass;

    public JavaClassBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
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
