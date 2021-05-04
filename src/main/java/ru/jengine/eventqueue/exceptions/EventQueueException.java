package ru.jengine.eventqueue.exceptions;

public class EventQueueException extends RuntimeException {
    public EventQueueException(String message) {
        super(message);
    }

    public EventQueueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
