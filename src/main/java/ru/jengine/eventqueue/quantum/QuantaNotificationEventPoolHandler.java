package ru.jengine.eventqueue.quantum;

import java.util.function.Consumer;

import ru.jengine.beancontainer.service.Constants;
import ru.jengine.eventqueue.event.Event;

public final class QuantaNotificationEventPoolHandler extends QuantumEventPoolHandler {

    public QuantaNotificationEventPoolHandler(QuantumEventPoolRegistrar quantumEventPoolRegistrar) {
        super(quantumEventPoolRegistrar, Constants.QUANTA_NOTIFICATION_QUEUE_CODE);
    }

    @Override
    public void handle() {
        QuantaEventPool eventPool = getEventPool();
        if (eventPool.countQuantumEventInPool() == 0) {
            return;
        }

        getEventProcessor().accept(eventPool.pool());
    }

    @Override
    protected void handle(Event event, Consumer<Event> eventProcessor) { }
}
