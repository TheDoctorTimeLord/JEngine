package ru.jengine.eventqueue.eventpool;

import java.util.ArrayDeque;
import java.util.Queue;

import ru.jengine.eventqueue.event.Event;

public class EventPoolQueue implements EventPool {
    private final Queue<Event> eventQueue = new ArrayDeque<>();
    private final String code;

    public EventPoolQueue(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Event pool() {
        Event result;
        synchronized (eventQueue) {
            result = eventQueue.poll();
        }
        return result;
    }

    @Override
    public <E extends Event> void registerEvent(E event) {
        synchronized (eventQueue) {
            eventQueue.add(event);
        }
    }
}
