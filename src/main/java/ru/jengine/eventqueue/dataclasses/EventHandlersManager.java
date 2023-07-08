package ru.jengine.eventqueue.dataclasses;

import java.util.List;

import ru.jengine.eventqueue.EventHandlerRegistrar;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;

public interface EventHandlersManager extends EventHandlerRegistrar {
    List<PreHandler<Event>> getPreHandlers(Event event);
    List<PostHandler<Event>> getPostHandlers(Event event);
}
