package ru.jengine.eventqueue.eventpool;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;

public abstract class ComplexEventPoolQueueHandler implements EventPoolHandler, EventInterceptor {
    private final String eventPoolCode;
    private Consumer<Event> eventProcessor;
    private EventPool eventPool;

    public ComplexEventPoolQueueHandler(String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
    }

    @Override
    public @Nullable String getEventPoolCode() {
        return eventPoolCode;
    }

    @Override
    public EventPool initialize(EventHandlingContext context) {
        this.eventProcessor = context.getEventProcessor();
        this.eventPool = new EventPoolQueue();
        return eventPool;
    }

    @Override
    public void handle() {
        Event event = eventPool.pool();
        while (event != null) {
            eventProcessor.accept(event);
            event = eventPool.pool();
        }
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        poolProvider.getEventPool(getEventPoolCode()).registerEvent(event);
    }
}
