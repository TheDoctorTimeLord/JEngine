package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;

public interface ModuleFactory {
    Module createModule(Class<?> moduleClass, ContainerConfiguration configuration);
}
