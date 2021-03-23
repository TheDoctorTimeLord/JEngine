package ru.jengine.beancontainer;

public interface BeanContainer {
    void initialize(Class<?> mainModule, ClassFinder classFinder, Object... additionalBeans);
}
