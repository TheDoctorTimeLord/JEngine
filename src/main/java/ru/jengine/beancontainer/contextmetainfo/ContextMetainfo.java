package ru.jengine.beancontainer.contextmetainfo;


import ru.jengine.beancontainer.beandefinitions.BeanDefinition;

import java.util.List;

public interface ContextMetainfo {
    List<BeanDefinition> extractBeanDefinitions();
    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();
}
