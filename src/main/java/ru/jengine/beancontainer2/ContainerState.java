package ru.jengine.beancontainer2;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer2.statepublisher.ContainerStatePublisher;

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
