package ru.jengine.utils.hierarchywalker.events;

public abstract class AbstractEventHandler<E extends Event, C> implements EventHandler<E> {
    private final C context;

    public AbstractEventHandler(C context) {
        this.context = context;
    }

    @Override
    public void handle(E event) {
        handle(event, context);
    }

    protected abstract void handle(E event, C context);
}
