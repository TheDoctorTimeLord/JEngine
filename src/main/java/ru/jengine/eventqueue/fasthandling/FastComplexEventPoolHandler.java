package ru.jengine.eventqueue.fasthandling;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.EventProcessor;
import ru.jengine.eventqueue.dataclasses.EventHandlersManager;
import ru.jengine.eventqueue.dataclasses.EventHandlersManagerWithoutHierarchy;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.eventpool.EventPool;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;

public abstract class FastComplexEventPoolHandler implements EventInterceptor, EventPoolHandler {
    private final String eventPoolCode;
    private EventHandlersManager eventHandlersManager;
    private EventProcessor eventProcessor;

    protected FastComplexEventPoolHandler(String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
    }

    @Override
    public String getEventPoolCode() {
        return eventPoolCode;
    }

    @Override
    @Nullable
    public EventPool initialize(EventHandlingContext context) {
        this.eventHandlersManager = prepareEventHandlersManager(context);
        this.eventProcessor = context.getEventProcessor();
        return null;
    }

    protected EventHandlersManagerWithoutHierarchy prepareEventHandlersManager(EventHandlingContext context) {
        return new EventHandlersManagerWithoutHierarchy(context.getPreHandlers());
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        eventProcessor.process(
                eventHandlersManager.getPreHandlers(event),
                eventHandlersManager.getPostHandlers(event),
                event
        );
    }

    @Override
    public void handle() {
        //Метод handle не должен отрабатывать, так как требуется чтобы событие было обработано в момент регистрации
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        eventHandlersManager.registerPostHandler(postHandler);
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        eventHandlersManager.removePostHandler(postHandler);
    }
}
