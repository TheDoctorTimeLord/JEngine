package ru.jengine.beancontainer.events;

import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public class RemoveContextEvent implements ContainerEvent {
    private final String contextName;

    public RemoveContextEvent(String contextName) {
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }
}
