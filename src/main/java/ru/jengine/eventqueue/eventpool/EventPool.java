package ru.jengine.eventqueue.eventpool;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.EventRegistrar;

public interface EventPool extends EventRegistrar {
    String getCode();
    Event pool();
}
