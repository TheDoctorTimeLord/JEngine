package ru.jengine.eventqueue;

public interface EventRegistrar {
    <E extends Event> void registerEvent(E event);
}
