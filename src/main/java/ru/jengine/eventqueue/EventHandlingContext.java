package ru.jengine.eventqueue;

import java.util.function.Consumer;

public class EventHandlingContext {
    private final Consumer<Event> eventProcessor;

    public EventHandlingContext(Consumer<Event> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public Consumer<Event> getEventProcessor() {
        return eventProcessor;
    }
}
