package ru.jengine.infrastructure.beancontainer.factory;

import ru.jengine.beancontainer.annotations.Inject;

public class TestedWithIncorrectInject {
    private final A a;

    @Inject
    public TestedWithIncorrectInject() {
        this(null);
    }

    @Inject
    public TestedWithIncorrectInject(A a) {
        this.a = a;
    }
}
