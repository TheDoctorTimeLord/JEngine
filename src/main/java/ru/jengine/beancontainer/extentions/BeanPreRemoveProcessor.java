package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.containercontext.ContainerContext;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(BeanData beanData, ContainerContext context);
}
