package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;

public interface ModuleFactory {
    Module createAnnotatedModule(Class<?> moduleClass, ContainerConfiguration configuration);
    Module configureModule(Module module, ModuleContext moduleContext);
}
