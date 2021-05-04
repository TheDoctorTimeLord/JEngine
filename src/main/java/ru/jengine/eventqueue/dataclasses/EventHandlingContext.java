package ru.jengine.eventqueue.dataclasses;

import java.util.function.Consumer;

import ru.jengine.eventqueue.event.Event;

public class EventHandlingContext {
    private final Consumer<Event> eventProcessor;

    public EventHandlingContext(Consumer<Event> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public Consumer<Event> getEventProcessor() {
        return eventProcessor;
    }
}
