package ru.jengine.eventqueue.eventpool;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.PostHandler;

public interface EventPoolHandler {
    String getEventPoolCode();
    @Nullable EventPool initialize(EventHandlingContext context);
    void handle();

    void registerPostHandler(PostHandler<?> postHandler);
    void removePostHandler(PostHandler<?> postHandler);
}
