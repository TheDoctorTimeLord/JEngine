package ru.jengine.beancontainer2.intstructure.pac4;

import ru.jengine.beancontainer2.annotations.Bean;

@Bean
public class C {
    private final A a;
    private final B b;

    public C(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
