package ru.test.annotation;

import ru.jengine.eventqueue.Event;

public class AsyncEventMessage implements Event {
    private final String message;

    public AsyncEventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
