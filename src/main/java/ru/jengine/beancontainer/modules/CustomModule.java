package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.beandefinitionreades.BeanDefinitionReader;
import ru.jengine.beancontainer.beandefinitionreades.ExistingBeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;

public class CustomModule implements Module {
    private final String contextName;
    private final List<Object> externalBeans;

    public CustomModule(String contextName, List<Object> externalBeans) {
        this.contextName = contextName;
        this.externalBeans = new ArrayList<>(externalBeans);
    }

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return List.of(new ExistingBeanDefinitionReader(externalBeans));
    }

    @Override
    public void configure(ModuleContext context) { }

    @Override
    public String getContextName() {
        return contextName;
    }

    @Override
    public List<String> getBeanSources() {
        return List.of();
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return false;
    }

    @Override
    public List<Class<?>> getSubmodules() {
        return List.of();
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> interfaceCls) {
        return List.of();
    }
}
