package ru.jengine.eventqueue.event;

public interface PreHandler <E extends Event> extends EventHandler<E> {
    boolean isValid(E event);
}
