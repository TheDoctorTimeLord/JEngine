package ru.jengine.eventqueue;

import ru.jengine.eventqueue.event.PostHandler;

public interface PollableEventHandlerRegistrar extends EventHandlerRegistrar {
    void registerPostHandlerToPool(String eventPoolCode, PostHandler<?> postHandler);
    void removePostHandlerFromPool(String eventPoolCode, PostHandler<?> postHandler);
}
