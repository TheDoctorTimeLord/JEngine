package ru.jengine.eventqueue;

public interface EventInterceptor {
    boolean isValid(Event event);
    void intercept(Event event, EventPoolProvider poolProvider);
}
