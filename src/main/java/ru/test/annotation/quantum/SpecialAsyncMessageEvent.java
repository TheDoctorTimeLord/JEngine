package ru.test.annotation.quantum;

import ru.jengine.eventqueue.event.Event;

public class SpecialAsyncMessageEvent implements Event {
    private final String message;

    public SpecialAsyncMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return "Special: " + message;
    }
}
