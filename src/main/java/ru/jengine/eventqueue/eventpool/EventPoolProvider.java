package ru.jengine.eventqueue.eventpool;

import javax.annotation.Nullable;

public interface EventPoolProvider {
    @Nullable
    EventPool getEventPool(String eventPoolCode);
}
