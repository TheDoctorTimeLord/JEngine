package ru.jengine.beancontainer2.contextmetainfo;

import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.ContainerContext;

public interface ContainerContextFactory {
    ContainerContext build(ContextMetainfo metainfo, BeanFactory beanFactory);
}
