package ru.jengine.beancontainer.implementation.moduleimpls;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.implementation.beandefinitionreaders.ExistBeanDefinitionsReader;
import ru.jengine.beancontainer.service.Constants.Contexts;
import ru.jengine.utils.ClassUtils;

public class ExistBeansModule implements Module {
    private final List<Object> beans;
    private final String contextName;

    public ExistBeansModule(List<Object> beans) {
        this(beans, Contexts.EXTERNAL_BEANS_CONTEXT);
    }

    public ExistBeansModule(List<Object> beans, String contextName) {
        this.beans = beans;
        this.contextName = contextName;
    }

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return Collections.singletonList(new ExistBeanDefinitionsReader(beans));
    }

    @Override
    public void configure(ModuleContext context) { }

    @Override
    public String getContextName() {
        return contextName;
    }

    @Override
    public List<String> getBeanSources() {
        return Collections.emptyList();
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return true;
    }

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
