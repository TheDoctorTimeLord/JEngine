package ru.jengine.beancontainer.containercontext;

import ru.jengine.beancontainer.exceptions.ContainerException;

import java.util.Collection;

public class ResolvedBeanData extends BeanData {
    public static final ResolvedBeanData NOT_RESOLVED = new ResolvedBeanData(null, null);

    private final boolean isMultipleBean;

    public ResolvedBeanData(Object beanValue, Class<?> beanBaseClass) {
        this(beanValue, beanBaseClass, false);
    }

    public ResolvedBeanData(Object beanValue, Class<?> beanBaseClass, boolean isMultipleBean) {
        super(beanValue, beanBaseClass);
        this.isMultipleBean = isMultipleBean;
    }

    public boolean isMultipleBean() {
        return isMultipleBean;
    }

    @SuppressWarnings("unchecked")
    public Collection<ResolvedBeanData> asMultipleBeans() {
        if (beanValue instanceof Collection<?> collection) {
            return (Collection<ResolvedBeanData>) collection;
        }

        Class<?> beanValueClass = beanValue != null ? beanValue.getClass() : null;
        throw new ContainerException(("Bean value [%s] can not be present as some beans. Available collection of BeanData")
                .formatted(beanValueClass));
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> asMultipleBeans(Class<T> beanClass) {
        if (beanValue instanceof Collection<?> collection) {
            return (Collection<T>) collection;
        }

        Class<?> beanValueClass = beanValue != null ? beanValue.getClass() : null;
        throw new ContainerException(("Bean value [%s] can not be present as some beans. Available collection of beans")
                .formatted(beanValueClass));
    }

    public boolean isResolved() {
        return this != NOT_RESOLVED;
    }

    @Override
    public String toString() {
        return "{beanClass=" + beanBaseClass +
                ", beanValue=" + beanValue +
                '}';
    }
}
