package ru.jengine.beancontainer.events;


import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public record RegisterContextEvent(String contextName, ContainerContext context) implements ContainerEvent {
}
