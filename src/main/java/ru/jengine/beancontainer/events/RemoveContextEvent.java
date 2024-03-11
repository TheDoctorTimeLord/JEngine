package ru.jengine.beancontainer.events;

import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public record RemoveContextEvent(String contextName, ContainerContext removedContext) implements ContainerEvent {
}
