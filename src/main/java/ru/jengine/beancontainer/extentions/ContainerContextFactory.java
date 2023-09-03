package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;

public interface ContainerContextFactory {
    ContainerContext build(String builtContextName, ContextMetainfo metainfo, BeanFactory beanFactory,
            ContainerState containerState);
}
