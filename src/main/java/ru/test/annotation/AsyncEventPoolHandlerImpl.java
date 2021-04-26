package ru.test.annotation;

import java.util.function.Consumer;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.AsyncEventPoolHandler;
import ru.jengine.eventqueue.Event;
import ru.jengine.eventqueue.EventHandlingContext;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.EventPool;
import ru.jengine.eventqueue.EventPoolProvider;
import ru.jengine.eventqueue.EventPoolQueue;

@Bean
public class AsyncEventPoolHandlerImpl implements AsyncEventPoolHandler, EventInterceptor {
    private Consumer<Event> eventProcessor;
    private EventPoolQueue eventQueue;

    @Override
    public EventPool initialize(EventHandlingContext context) {
        eventProcessor = context.getEventProcessor();
        eventQueue = new EventPoolQueue("async");
        return eventQueue;
    }

    @Override
    public void handle() {
        Event eventToHandle = eventQueue.pool();
        while (eventToHandle != null) {
            eventProcessor.accept(eventToHandle);
            eventToHandle = eventQueue.pool();
        }
    }

    @Override
    public boolean isValid(Event event) {
        return event instanceof AsyncEventMessage;
    }

    @Override
    public void intercept(Event event, EventPoolProvider poolProvider) {
        poolProvider.getEventPool("async").registerEvent(event);
    }
}
