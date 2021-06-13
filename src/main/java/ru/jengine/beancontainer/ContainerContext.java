package ru.jengine.beancontainer;

import java.util.List;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface ContainerContext {
    void initialize(List<Module> modules, BeanFactory factory);
    void preProcessBeans(List<ContextPreProcessor> contextPreProcessors);
    void prepareBeans();

    void reload();
    void prepareToRemove();

    @Nullable
    BeanContext getBean(Class<?> beanClass);
    boolean containsBean(Class<?> beanClass);
    void deleteBean(Object bean);
}
