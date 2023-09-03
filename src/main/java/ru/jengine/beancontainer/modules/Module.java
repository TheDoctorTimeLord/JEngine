package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.beandefinitionreades.BeanDefinitionReader;

import java.util.List;

public interface Module {
    List<BeanDefinitionReader> getBeanDefinitionReaders();
    void configure(ModuleContext context);

    String getContextName();
    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();

    List<Class<?>> getSubmodules();
    List<Class<?>> getImplementations(Class<?> interfaceCls);
}
