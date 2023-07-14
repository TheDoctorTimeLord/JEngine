package ru.jengine.beancontainer2.intstructure.factory;

import ru.jengine.beancontainer2.annotations.Inject;

public class TestedWithParameters {
    private final A a;
    private final B b;
    private final C c;

    public TestedWithParameters() {
        this(null, null, null);
    }

    @Inject
    public TestedWithParameters(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
