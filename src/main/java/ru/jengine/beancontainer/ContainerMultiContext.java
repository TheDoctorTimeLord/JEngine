package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.BeanContext;

public interface ContainerMultiContext extends ContainerContext {
    BeanContext getBean(String contextName, Class<?> beanClass);
    boolean containsBean(String contextName, Class<?> beanClass);
    void deleteBean(String contextName, Object bean);

    void registerContext(String name, ContainerContext context);
}
