package ru.jengine.eventqueue;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;

public interface EventInterceptor {
    boolean isValid(Event event);
    void intercept(Event event, EventPoolProvider poolProvider);
}
