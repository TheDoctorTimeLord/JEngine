package ru.jengine.eventqueue.event;

public interface EventRegistrar {
    <E extends Event> void registerEvent(E event);
}
