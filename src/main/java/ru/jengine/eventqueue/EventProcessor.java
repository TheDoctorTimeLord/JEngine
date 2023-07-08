package ru.jengine.eventqueue;

import java.util.List;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;

@FunctionalInterface
public interface EventProcessor {
    void process(List<PreHandler<Event>> preHandlers, List<PostHandler<Event>> postHandlers, Event event);
}
