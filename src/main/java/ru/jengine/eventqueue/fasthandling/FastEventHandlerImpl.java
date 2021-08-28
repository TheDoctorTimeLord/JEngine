package ru.jengine.eventqueue.fasthandling;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;
import ru.jengine.eventqueue.exceptions.EventQueueException;

public class FastEventHandlerImpl implements FastEventHandler {
    private final Map<String, FastEventHandlerNode> fastEventHandlerNodes = new ConcurrentHashMap<>();
    private final EventPoolProvider poolProvider;

    public FastEventHandlerImpl(EventPoolProvider poolProvider) {
        this.poolProvider = poolProvider;
    }

    @Override
    public void registerFastEventPoolHandler(String handlerCode,
            List<EventInterceptor> interceptors, EventPoolHandler handler)
    {
        synchronized (fastEventHandlerNodes) {
            if (fastEventHandlerNodes.containsKey(handlerCode)) {
                throw new EventQueueException("Fast event handler with code [" + handlerCode + "] already exists. "
                        + "Registered handler [" + handler + "]");
            }
            fastEventHandlerNodes.put(handlerCode, new FastEventHandlerNode(interceptors, handler, poolProvider));
        }
    }

    @Override
    public EventPoolHandler removeHandler(String handlerCode) {
        synchronized (fastEventHandlerNodes) {
            FastEventHandlerNode node = fastEventHandlerNodes.remove(handlerCode);
            return node == null ? null : node.getFastPoolHandler();
        }
    }

    @Override
    public <E extends Event> void handleNow(String handlerCode, E event) {
        FastEventHandlerNode node = fastEventHandlerNodes.get(handlerCode);
        if (node != null) {
            node.handleEventFast(event);
        }
    }
}
