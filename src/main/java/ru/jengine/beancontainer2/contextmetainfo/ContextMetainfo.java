package ru.jengine.beancontainer2.contextmetainfo;


import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.ContainerContext;

import java.util.List;

public interface ContextMetainfo {
    ContainerContext buildContext(BeanFactory factory);
    ContextMetainfo cloneWithContext(String newContextName);

    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();
}
