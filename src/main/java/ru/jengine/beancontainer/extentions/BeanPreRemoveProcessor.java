package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.containercontext.ContainerContext;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
