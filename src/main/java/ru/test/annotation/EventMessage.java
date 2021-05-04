package ru.test.annotation;

import ru.jengine.eventqueue.event.Event;

public class EventMessage implements Event {
    private final String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
