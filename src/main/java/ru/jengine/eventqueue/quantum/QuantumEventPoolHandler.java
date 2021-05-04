package ru.jengine.eventqueue.quantum;

import java.util.function.Consumer;

import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPool;

public abstract class QuantumEventPoolHandler implements AsyncEventPoolHandler {
    private final String eventPoolCode;
    private QuantaEventPool eventPool;
    private Consumer<Event> eventProcessor;
    private final QuantumEventPoolRegistrar quantumEventPoolRegistrar; //TODO спорный момент что делать с регистратором

    protected QuantumEventPoolHandler(QuantumEventPoolRegistrar quantumEventPoolRegistrar, String eventPoolCode) {
        this.eventPoolCode = eventPoolCode;
        this.quantumEventPoolRegistrar = quantumEventPoolRegistrar;
    }

    @Override
    public EventPool initialize(EventHandlingContext context) {
        eventProcessor = context.getEventProcessor();
        eventPool = new EventPoolQueueWithQuanta(eventPoolCode);
        quantumEventPoolRegistrar.registerQuantumEventPool(eventPool);
        return eventPool;
    }

    @Override
    public void handle() {
        if (eventPool.countQuantumEventInPool() == 0) {
            return;
        }

        Event event;
        do {
            event = eventPool.pool();
            handle(event, eventProcessor);
        } while (!(event instanceof QuantumEvent));
    }

    protected abstract void handle(Event event, Consumer<Event> eventProcessor);
}
