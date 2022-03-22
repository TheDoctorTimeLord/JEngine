package ru.jengine.eventqueue;

import java.util.List;

import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;

public interface EventPoolHandlersManager {
    void registerEventPoolHandler(List<EventInterceptor> eventInterceptors, EventPoolHandler eventPoolHandler,
            List<PreHandler<?>> additionalPreHandlers);
    void removeEventPoolHandler(List<EventInterceptor> eventInterceptors, String eventPoolHandlerCode);
}
