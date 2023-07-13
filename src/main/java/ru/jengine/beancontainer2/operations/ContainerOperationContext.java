package ru.jengine.beancontainer2.operations;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;

public class ContainerOperationContext {
    private final ContainerConfiguration configuration;

    public ContainerOperationContext(ContainerConfiguration configuration) {
        this.configuration = configuration;
    }

    public ContainerConfiguration getContainerConfiguration() {
        return configuration;
    }
}
