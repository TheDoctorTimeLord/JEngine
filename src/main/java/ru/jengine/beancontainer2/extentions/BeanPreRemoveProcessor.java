package ru.jengine.beancontainer2.extentions;

import ru.jengine.beancontainer2.containercontext.ContainerContext;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(Object bean, Class<?> beanClass, ContainerContext context);
}
