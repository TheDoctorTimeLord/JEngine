package ru.jengine.beancontainer;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface ContainerMultiContext extends ContainerContext {
    @Nullable
    BeanContext getBean(String contextName, Class<?> beanClass);
    boolean containsBean(String contextName, Class<?> beanClass);

    @Nullable
    ContainerContext getContext(String name);

    void registerContext(String name, ContainerContext context);
    void removeContext(String name);
    void reloadContext(String name);
}
