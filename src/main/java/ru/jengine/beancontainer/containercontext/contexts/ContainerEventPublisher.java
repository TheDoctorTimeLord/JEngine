package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public interface ContainerEventPublisher {
    void publish(ContainerEvent event);
}
