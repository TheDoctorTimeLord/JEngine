package ru.jengine.beancontainer;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.statepublisher.ContainerEventDispatcher;

public class ContainerState {
    private final ContainerConfiguration configuration;
    private final ContainerContextFacade containerContextFacade;
    private final ContextMetainfoManager contextMetainfoManager;
    private final ContainerEventDispatcher containerEventDispatcher;

    public ContainerState(ContainerConfiguration configuration, ContainerContextFacade containerContextFacade,
            ContextMetainfoManager contextMetainfoManager, ContainerEventDispatcher containerEventDispatcher)
    {
        this.configuration = configuration;
        this.containerContextFacade = containerContextFacade;
        this.contextMetainfoManager = contextMetainfoManager;
        this.containerEventDispatcher = containerEventDispatcher;
    }

    public ContainerConfiguration getContainerConfiguration() {
        return configuration;
    }

    public ContainerContextFacade getContainerContextFacade() {
        return containerContextFacade;
    }

    public ContextMetainfoManager getContextMetainfoManager() {
        return contextMetainfoManager;
    }

    public ContainerEventDispatcher getContainerEventDispatcher() {
        return containerEventDispatcher;
    }
}
