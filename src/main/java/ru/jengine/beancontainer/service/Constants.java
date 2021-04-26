package ru.jengine.beancontainer.service;

import ru.jengine.beancontainer.MainInfrastructureModule;
import ru.jengine.beancontainer.MainModule;

public interface Constants {
    String DEFAULT_CONTEXT = "default";
    String INFRASTRUCTURE_CONTEXT = "infrastructure";
    Class<?> BEAN_CONTAINER_MAIN_MODULE = MainModule.class;
    Class<?> BEAN_CONTAINER_MAIN_INFRASTRUCTURE_MODULE = MainInfrastructureModule.class;
}
