package ru.test.annotation.quantum;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.eventqueue.quantum.QuantumEvent;

@Bean
public class QuantumEventHandler implements PreHandler<QuantumEvent> {
    @Override
    public Class<QuantumEvent> getHandlingEventType() {
        return QuantumEvent.class;
    }

    @Override
    public void handle(QuantumEvent event) {
        System.out.println("QUANTUM EVENT!");
    }

    @Override
    public boolean isValid(QuantumEvent event) {
        return true;
    }
}
