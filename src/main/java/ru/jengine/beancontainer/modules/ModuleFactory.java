package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;

public interface ModuleFactory {
    Module createAnnotatedModule(Class<?> moduleClass, ContainerConfiguration configuration);
    ClassFinder extractClassFinderFromModule(Class<?> moduleClass, ContainerConfiguration configuration);
    Module configureModule(Module module, ModuleContext moduleContext);
}
