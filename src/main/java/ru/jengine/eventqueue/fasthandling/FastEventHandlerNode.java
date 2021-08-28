package ru.jengine.eventqueue.fasthandling;

import java.util.List;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;

public class FastEventHandlerNode {
    private final List<EventInterceptor> fastInterceptors;
    private final EventPoolHandler fastPoolHandler;
    private final EventPoolProvider poolProvider;

    public FastEventHandlerNode(List<EventInterceptor> fastInterceptors, EventPoolHandler fastPoolHandler,
            EventPoolProvider poolProvider)
    {
        this.fastInterceptors = fastInterceptors;
        this.fastPoolHandler = fastPoolHandler;
        this.poolProvider = poolProvider;
    }

    public <E extends Event> void handleEventFast(E event) {
        if (fastInterceptors.isEmpty()) {
            return;
        }

        registerEvent(event);
        fastPoolHandler.handle();
    }

    private <E extends Event> void registerEvent(E event) {
        for (EventInterceptor interceptor : fastInterceptors) {
            if (interceptor.isValid(event)) {
                interceptor.intercept(event, poolProvider);
            }
        }
    }

    public EventPoolHandler getFastPoolHandler() {
        return fastPoolHandler;
    }
}
