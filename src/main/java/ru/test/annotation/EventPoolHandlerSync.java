package ru.test.annotation;

import java.util.function.Consumer;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.Event;
import ru.jengine.eventqueue.EventHandlingContext;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.EventPool;
import ru.jengine.eventqueue.EventPoolHandler;
import ru.jengine.eventqueue.EventPoolProvider;
import ru.jengine.eventqueue.EventPoolQueue;

@Bean
public class EventPoolHandlerSync implements EventPoolHandler, EventInterceptor {
    private Consumer<Event> eventProcessor;
    private EventPool eventPool;

    @Override
    public EventPool initialize(EventHandlingContext context) {
        eventProcessor = context.getEventProcessor();
        eventPool = new EventPoolQueue("code1");
        return eventPool;
    }

    @Override
    public void handle() {
        Event eventToHandle = eventPool.pool();
        while (eventToHandle != null) {
            eventProcessor.accept(eventToHandle);
            eventToHandle = eventPool.pool();
        }
    }

    @Override
    public boolean isValid(Event event) {
        return event instanceof EventMessage;
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        poolProvider.getEventPool("code1").registerEvent(event);
    }
}
