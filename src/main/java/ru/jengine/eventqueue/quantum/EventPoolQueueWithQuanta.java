package ru.jengine.eventqueue.quantum;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.exceptions.EventQueueException;

public class EventPoolQueueWithQuanta implements QuantaEventPool {
    private final Queue<Event> eventQueue = new ArrayDeque<>();
    private final Lock queueLock = new ReentrantLock();
    private int countQuantumEvent;

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
                if (countQuantumEvent < 0) {
                    throw new EventQueueException("Count of QuantumEvents less then 0");
                }
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
