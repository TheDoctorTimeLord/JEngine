package ru.jengine.eventqueue.dataclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.utils.serviceclasses.HasPriority;
import ru.jengine.utils.serviceclasses.SortedMultiset;

public class EventHandlersManagerWithoutHierarchy implements EventHandlersManager {
    private final Map<Class<?>, List<PreHandler<Event>>> preHandlers = new HashMap<>();
    private final Map<Class<?>, SortedMultiset<PostHandler<Event>>> postHandlers = new ConcurrentHashMap<>();

    public EventHandlersManagerWithoutHierarchy(List<PreHandler<?>> preHandlers) {
        for(PreHandler<?> preHandler : preHandlers) {
            PreHandler<Event> handler = (PreHandler<Event>)preHandler;
            Class<?> handlingEventType = handler.getHandlingEventType();
            this.preHandlers.computeIfAbsent(handlingEventType, cls -> new ArrayList<>()).add(handler);
        }
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        PostHandler<Event> handler = (PostHandler<Event>)postHandler;
        Class<?> handlingEventType = handler.getHandlingEventType();
        postHandlers.computeIfAbsent(handlingEventType, cls -> new SortedMultiset<>(HasPriority::getPriority))
                .add(handler);
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        PostHandler<Event> handler = (PostHandler<Event>)postHandler;
        synchronized (postHandlers) {
            Class<?> handledEventType = handler.getHandlingEventType();
            SortedMultiset<PostHandler<Event>> postHandlerMultiset = postHandlers.get(handledEventType);
            if (postHandlerMultiset == null) {
                return;
            }

            postHandlerMultiset.remove(handler);

            if (postHandlerMultiset.isEmpty()) {
                postHandlers.remove(handledEventType);
            }
        }
    }

    @Override
    public List<PreHandler<Event>> getPreHandlers(Event event) {
        return getPreHandlers(event.getClass());
    }

    protected List<PreHandler<Event>> getPreHandlers(Class<?> eventClass) {
        return preHandlers.getOrDefault(eventClass, Collections.emptyList());
    }

    @Override
    public List<PostHandler<Event>> getPostHandlers(Event event) {
        return getPostHandlers(event.getClass()).getSortedElements();
    }

    protected SortedMultiset<PostHandler<Event>> getPostHandlers(Class<?> eventClass) {
        return postHandlers.computeIfAbsent(eventClass, cls -> new SortedMultiset<>());
    }
}
