package ru.jengine.beancontainer.service;

import ru.jengine.beancontainer.MainInfrastructureModule;
import ru.jengine.beancontainer.MainModule;

public interface Constants {
    String DEFAULT_CONTEXT = "default";
    String INFRASTRUCTURE_CONTEXT = "infrastructure";
    String BATTLE_CONTEXT = "battle";

    Class<?> BEAN_CONTAINER_MAIN_MODULE = MainModule.class;
    Class<?> BEAN_CONTAINER_MAIN_INFRASTRUCTURE_MODULE = MainInfrastructureModule.class;
    String QUANTA_NOTIFICATION_QUEUE_CODE = "quantaNotificationQueueCode";
    String DISPATCHER_ASYNC_NAME = "dispatcherAsync";

    interface BeanStrategy {
        String SINGLETON = "singleton";
        String PROTOTYPE = "prototype";
    }
}
