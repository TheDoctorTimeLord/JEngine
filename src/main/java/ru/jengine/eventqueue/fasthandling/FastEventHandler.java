package ru.jengine.eventqueue.fasthandling;

import java.util.List;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;

public interface FastEventHandler {
    void registerFastEventPoolHandler(String handlerCode, List<EventInterceptor> interceptors, EventPoolHandler handler);
    EventPoolHandler removeHandler(String handlerCode);
    <E extends Event> void handleNow(String handlerCode, E event);
}
