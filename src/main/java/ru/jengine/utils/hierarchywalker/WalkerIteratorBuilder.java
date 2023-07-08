package ru.jengine.utils.hierarchywalker;

import java.util.function.Consumer;

public class WalkerIteratorBuilder {
    private static final Class<?>[] EMPTY_CLASSES = new Class[0];
    private static final Consumer<HierarchyElement> DEFAULT_CALLBACK = e -> {};

    private final Class<?> startWalkingClass;
    private boolean withTypeChecking = false;
    private boolean withGenericMapping = true;
    private Class<?>[] initialGenericParameters = EMPTY_CLASSES;
    private Consumer<HierarchyElement> onGoBackByStarted = DEFAULT_CALLBACK;

    public static WalkerIteratorBuilder builder(Class<?> startWalkingClass) {
        return new WalkerIteratorBuilder(startWalkingClass);
    }

    private WalkerIteratorBuilder(Class<?> startWalkingClass) {
        this.startWalkingClass = startWalkingClass;
    }

    public WalkerIteratorBuilder withTypeChecking(boolean withTypeChecking) {
        this.withTypeChecking = withTypeChecking;
        return this;
    }

    public WalkerIteratorBuilder withGenericMapping(boolean withGenericMapping) {
        this.withGenericMapping = withGenericMapping;
        return this;
    }

    public WalkerIteratorBuilder initialGenericParameters(Class<?>... initialGenericParameters) {
        this.initialGenericParameters = initialGenericParameters;
        return this;
    }

    public WalkerIteratorBuilder onGoBackByStarted(Consumer<HierarchyElement> onGoBackByStarted) {
        this.onGoBackByStarted = onGoBackByStarted;
        return this;
    }

    public WalkerIterator build() {
        return new WalkerIterator(startWalkingClass, withTypeChecking, withGenericMapping, initialGenericParameters, onGoBackByStarted);
    }
}
