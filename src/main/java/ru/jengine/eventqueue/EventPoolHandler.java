package ru.jengine.eventqueue;

public interface EventPoolHandler {
    EventPool initialize(EventHandlingContext context);
    void handle();
}
