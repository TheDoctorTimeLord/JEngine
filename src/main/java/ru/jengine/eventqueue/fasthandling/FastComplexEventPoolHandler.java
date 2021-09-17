package ru.jengine.eventqueue.fasthandling;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPool;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;

public abstract class FastComplexEventPoolHandler implements EventInterceptor, EventPoolHandler {
    private Consumer<Event> eventProcessor;
    private Event handlingEvent;

    @Override
    public @Nullable String getEventPoolCode() {
        return null;
    }

    @Override
    public EventPool initialize(EventHandlingContext context) {
        this.eventProcessor = context.getEventProcessor();
        return null;
    }

    @Override
    public void handle() {
        if (handlingEvent != null) {
            eventProcessor.accept(handlingEvent);
        }
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        this.handlingEvent = event;
    }
}
