package ru.jengine.eventqueue.quantum;

import ru.jengine.eventqueue.eventpool.EventPool;

public interface QuantaEventPool extends EventPool {
    void lockEventPool();
    void unlockEventPool();
    void registerQuantumEvent(QuantumEvent event);
    int countQuantumEventInPool();
}
