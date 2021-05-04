package ru.test.annotation;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.event.PreHandler;

@Bean
public class AsyncEventMessageHandler implements PreHandler<AsyncEventMessage> {
    @Override
    public Class<AsyncEventMessage> getHandlingEventType() {
        return AsyncEventMessage.class;
    }

    @Override
    public void handle(AsyncEventMessage event) {
        System.out.println(event.getMessage());
    }

    @Override
    public boolean isValid(AsyncEventMessage event) {
        return event.getMessage() != null;
    }
}
