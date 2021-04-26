package ru.jengine.eventqueue;

public interface EventHandlerRegistrar {
    void registerPostHandler(PostHandler<?> postHandler);
    void removePostHandler(PostHandler<?> postHandler);
}
