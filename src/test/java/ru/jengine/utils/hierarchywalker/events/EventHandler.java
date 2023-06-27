package ru.jengine.utils.hierarchywalker.events;

import ru.jengine.utils.HierarchyWalkingUtils;

public interface EventHandler<E extends Event> {
    default Class<?> getHandledEventType() {
        return HierarchyWalkingUtils.getGenericType(getClass(), EventHandler.class, 0);
    }

    void handle(E event);
}
