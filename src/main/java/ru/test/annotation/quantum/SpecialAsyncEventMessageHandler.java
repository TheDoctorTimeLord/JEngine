package ru.test.annotation.quantum;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.event.PreHandler;

@Bean
public class SpecialAsyncEventMessageHandler implements PreHandler<SpecialAsyncMessageEvent> {
    @Override
    public Class<SpecialAsyncMessageEvent> getHandlingEventType() {
        return SpecialAsyncMessageEvent.class;
    }

    @Override
    public void handle(SpecialAsyncMessageEvent event) {
        System.out.println(event.getMessage());
    }

    @Override
    public boolean isValid(SpecialAsyncMessageEvent event) {
        return event.getMessage() != null;
    }
}

