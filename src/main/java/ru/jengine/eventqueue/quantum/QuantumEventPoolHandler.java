package ru.jengine.eventqueue.quantum;

import javax.annotation.Nullable;

import ru.jengine.eventqueue.EventProcessor;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPool;

public abstract class QuantumEventPoolHandler implements AsyncEventPoolHandler {
    private final QuantaEventPool eventPool;
    private final String eventPoolCode;
    private EventProcessor eventProcessor;

    protected QuantumEventPoolHandler(QuantumEventPoolRegistrar quantumEventPoolRegistrar, String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
        this.eventPool = new EventPoolQueueWithQuanta();
        quantumEventPoolRegistrar.registerQuantumEventPool(eventPool);
    }

    @Override
    public String getEventPoolCode() {
        return eventPoolCode;
    }

    @Override
    @Nullable
    public EventPool initialize(EventHandlingContext context) {
        eventProcessor = context.getEventProcessor();
        return eventPool;
    }

    @Override
    public void handle() {
        if (eventPool.countQuantumEventInPool() == 0) {
            return;
        }

        Event event = eventPool.pool();
        while (event != null && !(event instanceof QuantumEvent)) {
            handle(event, eventProcessor);
            event = eventPool.pool();
        }
    }

    protected abstract void handle(Event event, EventProcessor eventProcessor);

    protected QuantaEventPool getEventPool() {
        return eventPool;
    }

    protected EventProcessor getEventProcessor() {
        return eventProcessor;
    }
}
