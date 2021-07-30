package ru.jengine.beancontainer;

import java.util.List;

import ru.jengine.beancontainer.dataclasses.ModuleContext;

public interface Module {
    List<BeanDefinitionReader> getBeanDefinitionReaders();
    void configure(ModuleContext context);

    String getContextName();
    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();

    List<Class<?>> getSubmodules();
    List<Class<?>> getImplementations(Class<?> interfaceCls);
}
