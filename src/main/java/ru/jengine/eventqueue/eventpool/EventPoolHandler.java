package ru.jengine.eventqueue.eventpool;

import ru.jengine.eventqueue.dataclasses.EventHandlingContext;

public interface EventPoolHandler {
    EventPool initialize(EventHandlingContext context);
    void handle();
}
