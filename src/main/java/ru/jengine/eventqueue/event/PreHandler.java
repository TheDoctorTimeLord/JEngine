package ru.jengine.eventqueue.event;

public interface PreHandler <E extends Event> extends EventHandler<E> { //TODO избавится, так как это бесполезно
    boolean isValid(E event);
}
