package ru.jengine.eventqueue;

import ru.jengine.eventqueue.event.PostHandler;

public interface EventHandlerRegistrar {
    void registerPostHandler(PostHandler<?> postHandler);
    void removePostHandler(PostHandler<?> postHandler);
}
