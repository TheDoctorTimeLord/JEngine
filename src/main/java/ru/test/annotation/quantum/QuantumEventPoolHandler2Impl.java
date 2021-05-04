package ru.test.annotation.quantum;

import java.util.function.Consumer;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;
import ru.jengine.eventqueue.quantum.QuantumEventPoolHandler;
import ru.jengine.eventqueue.quantum.QuantumEventPoolRegistrar;

@Bean
public class QuantumEventPoolHandler2Impl extends QuantumEventPoolHandler implements EventInterceptor {
    public QuantumEventPoolHandler2Impl(QuantumEventPoolRegistrar eventPoolRegistrar) {
        super(eventPoolRegistrar, "quantum2");
    }

    @Override
    public boolean isValid(Event event) {
        return event instanceof SpecialAsyncMessageEvent;
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        poolProvider.getEventPool("quantum2").registerEvent(event);
    }

    @Override
    protected void handle(Event event, Consumer<Event> eventProcessor) {
        eventProcessor.accept(event);
    }
}
