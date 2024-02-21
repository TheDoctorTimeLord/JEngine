package ru.jengine.beancontainer;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.ContextOperationChains;
import ru.jengine.beancontainer.operations.OperationsExecutor;
import ru.jengine.beancontainer.statepublisher.ContainerEventDispatcher;

import java.util.Collection;

public class JEngineContainer {
    private final ContainerState containerState;

    public JEngineContainer(ContainerConfiguration configuration) {
        ContainerContextFacade facade = new ContainerContextFacade();
        ContainerEventDispatcher eventDispatcher = new ContainerEventDispatcher();
        ContextMetainfoManager metainfoManager = new ContextMetainfoManager(configuration, facade, eventDispatcher);

        this.containerState = new ContainerState(configuration, facade, metainfoManager, eventDispatcher);
    }

    public void initializeContainerByDefault() {
        executeOperations(ContextOperationChains.INITIALIZE_CONTAINER);
    }

    public void executeOperations(ContainerOperation... operationsChain) {
        new OperationsExecutor(containerState, operationsChain).runOperationChain();
    }

    @SuppressWarnings("unchecked")
    public <T, R extends T> R getBean(Class<T> beanClass) {
        ResolvingProperties properties = ResolvingProperties.properties(beanClass);

        ResolvedBeanData searchResult = containerState.getContainerContextFacade().getBean(properties);

        return searchResult.isResolved() ? (R) searchResult.getBeanValue() : null;
    }

    @SuppressWarnings("unchecked")
    public <R, C extends Collection<?>> R getBean(Class<?> beanClass, Class<C> collectionClass) {
        ResolvingProperties properties = ResolvingProperties
                .properties(beanClass)
                .collectionClass(collectionClass);

        ResolvedBeanData searchResult = containerState.getContainerContextFacade().getBean(properties);

        return searchResult.isResolved() ? (R) searchResult.getBeanValue() : null;
    }

    @SuppressWarnings("unchecked")
    public <R> R getBean(String contextName, Class<?> beanClass) {
        return (R) containerState.getContainerContextFacade().getBean(ResolvingProperties
                .properties(beanClass)
                .beanContextSource(contextName)
        ).getBeanValue();
    }

    public void stop() {
        containerState.getContainerContextFacade().stop();
    }
}
