package ru.jengine.eventqueue;

public interface EventPoolProvider {
    EventPool getEventPool(String eventPoolCode);
}
