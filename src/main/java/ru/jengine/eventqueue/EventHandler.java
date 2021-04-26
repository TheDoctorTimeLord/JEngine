package ru.jengine.eventqueue;

public interface EventHandler <E extends Event> {
    Class<E> getHandlingEventType();
    void handle(E event);
}
