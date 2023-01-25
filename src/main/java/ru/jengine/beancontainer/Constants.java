package ru.jengine.beancontainer;

public interface Constants {
    Class<?> BEAN_CONTAINER_MAIN_MODULE = MainModule.class;
    Class<?> BEAN_CONTAINER_MAIN_INFRASTRUCTURE_MODULE = MainInfrastructureModule.class;
    String QUANTA_NOTIFICATION_QUEUE_CODE = "quantaNotificationQueueCode";
    String ASYNC_DISPATCHER_NAME = "asyncDispatcher";

    interface BeanStrategy {
        String SINGLETON = "singleton";
        String PROTOTYPE = "prototype";
    }

    interface Contexts {
        String DEFAULT_CONTEXT = "default";
        String INFRASTRUCTURE_CONTEXT = "infrastructure";
        String EXTERNAL_BEANS_CONTEXT = "externalBeans";

        String BATTLE_CONTEXT = "battle";
        String JSON_CONVERTER_CONTEXT = "jsonConverter";
    }
}
