package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.event.PreHandler;

@Bean
public class EventMessagePreHandler implements PreHandler<EventMessage> {
    @Override
    public Class<EventMessage> getHandlingEventType() {
        return EventMessage.class;
    }

    @Override
    public void handle(EventMessage event) {
        System.out.println(event.getMessage());
    }

    @Override
    public boolean isValid(EventMessage event) {
        return event.getMessage() != null;
    }
}
