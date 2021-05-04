package ru.jengine.eventqueue.event;

public interface EventHandler <E extends Event> {
    Class<E> getHandlingEventType();
    void handle(E event);
}
