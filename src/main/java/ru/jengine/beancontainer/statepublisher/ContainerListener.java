package ru.jengine.beancontainer.statepublisher;

import ru.jengine.beancontainer.ContainerState;

public interface ContainerListener<E extends ContainerEvent> {
    Class<E> getListenedEventClass();
    void handle(E event, ContainerState containerState);

    default void removeListener(ContainerState containerState) {
        containerState.getContainerEventDispatcher().removeListener(this);
    }
}
