package ru.jengine.beancontainer2.intstructure.pac5;

import ru.jengine.beancontainer2.annotations.Bean;

@Bean
public class E {
    private final boolean test;

    public E(D d) {
        this.test = !d.isTest2();
    }

    public boolean isTest() {
        return test;
    }
}
