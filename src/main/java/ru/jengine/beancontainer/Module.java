package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ModuleContext;

import java.util.List;

public interface Module {
    List<BeanDefinitionReader> getBeanDefinitionReaders();
    void configure(ModuleContext context);

    List<Class<?>> getSubmodules();
    List<Class<?>> getImplementations(Class<?> interfaceCls);
}
