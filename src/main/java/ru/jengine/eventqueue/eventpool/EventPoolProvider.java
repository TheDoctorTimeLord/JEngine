package ru.jengine.eventqueue.eventpool;

public interface EventPoolProvider {
    EventPool getEventPool(String eventPoolCode);
}
