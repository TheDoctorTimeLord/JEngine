package ru.jengine.battlemodule.core;

import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class SimpleIdGenerator implements IdGenerator {
    private volatile int currentId = Integer.MIN_VALUE;

    @Override
    public synchronized int generateId() {
        return currentId++;
    }
}
