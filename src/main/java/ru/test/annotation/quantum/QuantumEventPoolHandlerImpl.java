package ru.test.annotation.quantum;

import java.util.function.Consumer;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;
import ru.jengine.eventqueue.quantum.QuantumEventPoolHandler;
import ru.jengine.eventqueue.quantum.QuantumEventPoolRegistrar;
import ru.test.annotation.AsyncEventMessage;

@Bean
public class QuantumEventPoolHandlerImpl extends QuantumEventPoolHandler implements EventInterceptor {
    public QuantumEventPoolHandlerImpl(QuantumEventPoolRegistrar eventPoolRegistrar) {
        super(eventPoolRegistrar, "quantum1");
    }

    @Override
    public boolean isValid(Event event) {
        return event instanceof AsyncEventMessage;
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        poolProvider.getEventPool("quantum1").registerEvent(event);
    }

    @Override
    protected void handle(Event event, Consumer<Event> eventProcessor) {
        eventProcessor.accept(event);
    }
}
