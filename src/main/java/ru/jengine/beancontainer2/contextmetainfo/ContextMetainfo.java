package ru.jengine.beancontainer2.contextmetainfo;


import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.ContainerContext;

import java.util.List;

public interface ContextMetainfo {
    List<BeanDefinition> extractBeanDefinitions();
    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();
}
