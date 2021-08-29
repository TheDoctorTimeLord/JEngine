package ru.jengine.eventqueue.fasthandling;

import java.util.List;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;

public interface FastEventHandler {
    void registerFastHandler(String handlerCode, List<EventInterceptor> interceptors, EventPoolHandler handler);
    EventPoolHandler removeFastHandler(String handlerCode);
    <E extends Event> void handleNow(String handlerCode, E event);
}
