package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

import java.util.List;

public interface ContainerContext {
    void initialize(List<Module> modules, BeanFactory factory);
    void prepareBeans();

    BeanContext getBean(Class<?> beanClass);
    boolean containsBean(Class<?> beanClass);
    void deleteBean(Object bean);
}
