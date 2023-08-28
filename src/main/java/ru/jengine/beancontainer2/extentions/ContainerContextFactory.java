package ru.jengine.beancontainer2.extentions;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;

public interface ContainerContextFactory {
    ContainerContext build(ContextMetainfo metainfo, BeanFactory beanFactory, ContainerState containerState);
}
