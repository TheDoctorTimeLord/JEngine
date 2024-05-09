package ru.jengine.infrastructure.beancontainer.pac12;

import ru.jengine.beancontainer.annotations.AfterInitialize;
import ru.jengine.beancontainer.annotations.Bean;

@Bean
public class T {
    private boolean test = false;

    @AfterInitialize
    private void init() {
        test = true;
    }

    public boolean test()
    {
        return test;
    }
}
