package ru.jengine.infrastructure.beancontainer.factory;

public class TestedWithoutInject {
    private final A a;

    public TestedWithoutInject(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
