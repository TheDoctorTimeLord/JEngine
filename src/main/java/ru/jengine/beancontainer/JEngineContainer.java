package ru.jengine.beancontainer;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationsExecutor;
import ru.jengine.beancontainer.operations.defaultimpl.*;
import ru.jengine.beancontainer.statepublisher.ContainerEventDispatcher;

import java.util.Collection;

public class JEngineContainer {
    private final ContainerState operationState;

    public JEngineContainer(ContainerConfiguration configuration) {
        ContainerContextFacade facade = new ContainerContextFacade();
        ContainerEventDispatcher eventDispatcher = new ContainerEventDispatcher();
        ContextMetainfoManager metainfoManager = new ContextMetainfoManager(configuration, facade, eventDispatcher);

        this.operationState = new ContainerState(configuration, facade, metainfoManager, eventDispatcher);
    }

    public void initializeContainerByDefault() {
        executeOperations(
                new ModuleFinderOperation(),
                new PreloadContextOperation(Constants.Contexts.INFRASTRUCTURE_CONTEXT),
                new PreloadContextOperation(Constants.Contexts.EXTERNAL_BEANS_CONTEXT),
                new PresetContextFacadeOperation(),
                new ContainerMetainfoRegistrarOperation(),
                new StartingInitializeContextOperation(),
                new FinishingInitializeContextOperation()
        );
    }

    public void executeOperations(ContainerOperation... operationsChain) {
        new OperationsExecutor(operationState, operationsChain).runOperationChain();
    }

    @SuppressWarnings("unchecked")
    public <T, R extends T> R getBean(Class<T> beanClass) {
        ResolvingProperties properties = ResolvingProperties.properties(beanClass);

        ResolvedBeanData searchResult = operationState.getContainerContextFacade().getBean(properties);

        return searchResult.isResolved() ? (R) searchResult.getBeanValue() : null;
    }

    @SuppressWarnings("unchecked")
    public <R, C extends Collection<?>> R getBean(Class<?> beanClass, Class<C> collectionClass) {
        ResolvingProperties properties = ResolvingProperties
                .properties(beanClass)
                .collectionClass(collectionClass);

        ResolvedBeanData searchResult = operationState.getContainerContextFacade().getBean(properties);

        return searchResult.isResolved() ? (R) searchResult.getBeanValue() : null;
    }

    @SuppressWarnings("unchecked")
    public <R> R getBean(String contextName, Class<?> beanClass) {
        return (R) operationState.getContainerContextFacade().getBean(ResolvingProperties
                .properties(beanClass)
                .beanContextSource(contextName)
        ).getBeanValue();
    }

    public void stop() {
        operationState.getContainerContextFacade().stop();
    }
}
