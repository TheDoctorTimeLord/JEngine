package ru.jengine.beancontainer.implementation.moduleimpls;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.implementation.beandefinitionreaders.ExistBeanDefinitionsReader;
import ru.jengine.beancontainer.utils.ClassUtils;

public class ExistBeansModule implements Module {
    private final List<Object> beans;
    private List<BeanDefinitionReader> readers;

    public ExistBeansModule(List<Object> beans) {
        this.beans = beans;
    }

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return Collections.singletonList(new ExistBeanDefinitionsReader(beans));
    }

    @Override
    public void configure(ModuleContext context) { }

    @Override
    public List<Class<?>> getSubmodules() {
        return Collections.emptyList();
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> interfaceCls) {
        return beans.stream()
                .map(Object::getClass)
                .filter(cls -> ClassUtils.hasInterface(cls, interfaceCls))
                .collect(Collectors.toList());
    }
}
