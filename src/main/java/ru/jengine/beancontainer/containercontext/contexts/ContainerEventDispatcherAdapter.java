package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public class ContainerEventDispatcherAdapter implements ContainerEventPublisher {
    private final ContainerState containerState;

    public ContainerEventDispatcherAdapter(ContainerState containerState) {
        this.containerState = containerState;
    }

    @Override
    public void publish(ContainerEvent event) {
        containerState.getContainerEventDispatcher().publish(event, containerState);
    }
}
