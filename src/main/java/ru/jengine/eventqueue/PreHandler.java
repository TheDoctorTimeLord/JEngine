package ru.jengine.eventqueue;

public interface PreHandler <E extends Event> extends EventHandler<E> {
    boolean isValid(E event);
}
