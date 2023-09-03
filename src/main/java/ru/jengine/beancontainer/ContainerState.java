package ru.jengine.beancontainer;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.statepublisher.ContainerStatePublisher;

public class ContainerState {
    private final ContainerConfiguration configuration;
    private final ContainerContextFacade containerContextFacade;
    private final ContextMetainfoManager contextMetainfoManager;
    private final ContainerStatePublisher containerStatePublisher;

    public ContainerState(ContainerConfiguration configuration, ContainerContextFacade containerContextFacade,
            ContextMetainfoManager contextMetainfoManager, ContainerStatePublisher containerStatePublisher)
    {
        this.configuration = configuration;
        this.containerContextFacade = containerContextFacade;
        this.contextMetainfoManager = contextMetainfoManager;
        this.containerStatePublisher = containerStatePublisher;
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

    public ContainerStatePublisher getContainerStatePublisher() {
        return containerStatePublisher;
    }
}
