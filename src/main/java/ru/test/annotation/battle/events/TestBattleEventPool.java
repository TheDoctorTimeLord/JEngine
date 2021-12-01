package ru.test.annotation.battle.events;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.eventpool.ComplexEventPoolQueueHandler;

@Bean
public class TestBattleEventPool extends ComplexEventPoolQueueHandler {
    public TestBattleEventPool() {
        super("battle");
    }

    @Override
    public boolean isValid(Event event) {
        return true;
    }
}
