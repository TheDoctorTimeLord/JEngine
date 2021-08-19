package ru.jengine.battlemodule;

import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class SimpleIdGenerator implements IdGenerator {
    private volatile int currentId = Integer.MIN_VALUE;

    @Override
    public synchronized int generateId() {
        return currentId++;
    }
}
