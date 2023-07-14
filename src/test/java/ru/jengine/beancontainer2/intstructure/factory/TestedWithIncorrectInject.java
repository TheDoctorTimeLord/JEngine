package ru.jengine.beancontainer2.intstructure.factory;

import ru.jengine.beancontainer2.annotations.Inject;

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
