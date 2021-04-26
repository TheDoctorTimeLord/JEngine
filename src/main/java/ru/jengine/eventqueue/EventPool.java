package ru.jengine.eventqueue;

public interface EventPool extends EventRegistrar {
    String getCode();
    Event pool();
}
