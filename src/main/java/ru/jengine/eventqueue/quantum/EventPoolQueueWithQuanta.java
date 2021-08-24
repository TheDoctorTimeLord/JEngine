package ru.jengine.eventqueue.quantum;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ru.jengine.utils.Preconditions;
import ru.jengine.eventqueue.event.Event;

public class EventPoolQueueWithQuanta implements QuantaEventPool {
    private final Queue<Event> eventQueue = new ArrayDeque<>();
    private final Lock queueLock = new ReentrantLock();
    private final String eventPoolCode;
    private int countQuantumEvent;

    public EventPoolQueueWithQuanta(String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
    }

    @Override
    public String getCode() {
        return eventPoolCode;
    }

    @Override
    public void lockEventPool() {
        queueLock.lock();
    }

    @Override
    public void unlockEventPool() {
        queueLock.unlock();
    }

    @Override
    public void registerQuantumEvent(QuantumEvent event) {
        eventQueue.add(event);
        countQuantumEvent++;
    }

    @Override
    public int countQuantumEventInPool() {
        try {
            queueLock.lock();
            return countQuantumEvent;
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public Event pool() {
        try {
            queueLock.lock();
            Event pooledEvent = eventQueue.poll();
            if (pooledEvent instanceof QuantumEvent) {
                countQuantumEvent--;
                Preconditions.checkState(countQuantumEvent >= 0, "Count of QuantumEvents less then 0");
            }
            return pooledEvent;
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public <E extends Event> void registerEvent(E event) {
        try {
            queueLock.lock();
            eventQueue.add(event);
            if (event instanceof QuantumEvent) {
                countQuantumEvent++;
            }
        } finally {
            queueLock.unlock();
        }
    }
}
