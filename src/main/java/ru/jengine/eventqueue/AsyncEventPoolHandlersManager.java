package ru.jengine.eventqueue;

import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;

public interface AsyncEventPoolHandlersManager {
    void registerEventPoolHandler(AsyncEventPoolHandler eventPoolHandler);
    void removeEventPoolHandler(String eventPoolHandlerCode);
}
