package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.beandefinitionreades.BeanDefinitionReader;

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
