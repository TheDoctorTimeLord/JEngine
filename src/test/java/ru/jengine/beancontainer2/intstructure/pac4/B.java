package ru.jengine.beancontainer2.intstructure.pac4;

import ru.jengine.beancontainer2.annotations.Bean;

@Bean
public class B {
    private final A a;

    public B(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
