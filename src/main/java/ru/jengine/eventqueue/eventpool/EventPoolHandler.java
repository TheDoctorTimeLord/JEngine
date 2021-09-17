package ru.jengine.eventqueue.eventpool;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.dataclasses.EventHandlingContext;

public interface EventPoolHandler {
    @Nullable String getEventPoolCode();
    EventPool initialize(EventHandlingContext context);
    void handle();
}
