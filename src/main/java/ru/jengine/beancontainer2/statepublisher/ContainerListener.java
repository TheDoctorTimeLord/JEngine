package ru.jengine.beancontainer2.statepublisher;

import ru.jengine.beancontainer2.ContainerState;

public interface ContainerListener<E extends ContainerEvent> {
    Class<E> getListenedEventClass();
    void handle(E event, ContainerState containerState);
}
