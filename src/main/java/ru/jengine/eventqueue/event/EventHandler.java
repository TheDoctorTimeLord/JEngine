package ru.jengine.eventqueue.event;

import ru.jengine.utils.HierarchyWalkingUtils;

public interface EventHandler <E extends Event> { //TODO научится обрабатывать событие с подсобытиями
    default Class<E> getHandlingEventType() {
        return (Class<E>)HierarchyWalkingUtils.getGenericType(getClass(), EventHandler.class, 0);
    }

    void handle(E event);
}
