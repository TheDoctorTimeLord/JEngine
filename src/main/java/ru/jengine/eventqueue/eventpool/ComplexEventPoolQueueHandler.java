package ru.jengine.eventqueue.eventpool;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.EventProcessor;
import ru.jengine.eventqueue.dataclasses.EventHandlersManager;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;

public abstract class ComplexEventPoolQueueHandler implements EventPoolHandler, EventInterceptor {
    private final String eventPoolCode;
    private EventHandlersManager eventHandlersManager;
    private EventProcessor eventProcessor;
    private EventPool eventPool;

    public ComplexEventPoolQueueHandler(String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
    }

    @Override
    public String getEventPoolCode() {
        return eventPoolCode;
    }

    @Override
    @Nullable
    public EventPool initialize(EventHandlingContext context) {
        this.eventProcessor = context.getEventProcessor();
        this.eventPool = new EventPoolQueue();
        this.eventHandlersManager = new EventHandlersManager(context.getPreHandlers());

        return eventPool;
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        eventHandlersManager.registerPostHandler(postHandler);
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        eventHandlersManager.removePostHandler(postHandler);
    }

    @Override
    public void handle() {
        Event event = eventPool.pool();
        while (event != null) {
            eventProcessor.process(
                    eventHandlersManager.getPreHandlers(event),
                    eventHandlersManager.getPostHandlers(event),
                    event
            );
            event = eventPool.pool();
        }
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        EventPool eventPool = poolProvider.getEventPool(getEventPoolCode());
        if (eventPool != null) {
            eventPool.registerEvent(event);
        }
    }
}
