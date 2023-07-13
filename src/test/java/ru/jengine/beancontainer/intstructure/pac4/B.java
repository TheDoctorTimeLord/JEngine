package ru.jengine.beancontainer.intstructure.pac4;

import ru.jengine.beancontainer.annotations.Bean;

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
