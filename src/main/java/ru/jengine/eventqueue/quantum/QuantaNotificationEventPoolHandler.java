package ru.jengine.eventqueue.quantum;

import java.util.Collections;

import ru.jengine.beancontainer.Constants;
import ru.jengine.eventqueue.EventProcessor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.PostHandler;

public final class QuantaNotificationEventPoolHandler extends QuantumEventPoolHandler { //TODO починить события квантования

    public QuantaNotificationEventPoolHandler(QuantumEventPoolRegistrar quantumEventPoolRegistrar) {
        super(quantumEventPoolRegistrar, Constants.QUANTA_NOTIFICATION_QUEUE_CODE);
    }

    @Override
    public void handle() {
        QuantaEventPool eventPool = getEventPool();
        if (eventPool.countQuantumEventInPool() == 0) {
            return;
        }

        getEventProcessor().process(Collections.emptyList(), Collections.emptyList(), eventPool.pool());
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) { } //TODO подумать нужны ли тут PostHandler'ы

    @Override
    public void removePostHandler(PostHandler<?> postHandler) { }

    @Override
    protected void handle(Event event, EventProcessor eventProcessor) { }
}
