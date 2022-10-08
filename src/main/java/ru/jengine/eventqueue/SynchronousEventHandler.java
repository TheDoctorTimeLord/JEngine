package ru.jengine.eventqueue;

public interface SynchronousEventHandler {
    void handleEvents();

    void handleEvents(String eventPoolCode);
}
