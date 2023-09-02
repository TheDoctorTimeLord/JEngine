package ru.jengine.beancontainer2;

public interface Constants {
    String QUANTA_NOTIFICATION_QUEUE_CODE = "quantaNotificationQueueCode";
    String ASYNC_DISPATCHER_NAME = "asyncDispatcher";

    interface BeanScope {
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
