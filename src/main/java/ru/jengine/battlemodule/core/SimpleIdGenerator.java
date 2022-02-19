package ru.jengine.battlemodule.core;

import ru.jengine.beancontainer.annotations.Bean;

/**
 * Простая реализация {@link IdGenerator}, основанная на инкрементации целочисленного счётчика. Может использовать в
 * многопоточной среде
 */
@Bean
public class SimpleIdGenerator implements IdGenerator {
    private volatile int currentId = Integer.MIN_VALUE;

    @Override
    public synchronized int generateId() {
        return currentId++;
    }
}
