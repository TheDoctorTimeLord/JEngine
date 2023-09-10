package ru.jengine.beancontainer;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvingProperties;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationsExecutor;
import ru.jengine.beancontainer.operations.defaultimpl.*;
import ru.jengine.beancontainer.statepublisher.ContainerStatePublisher;

import java.util.Collection;

public class JEngineContainer {
    private final ContainerState operationState;

    public JEngineContainer(ContainerConfiguration configuration) {
        ContainerContextFacade facade = new ContainerContextFacade();
        ContainerStatePublisher publisher = new ContainerStatePublisher();
        ContextMetainfoManager metainfoManager = new ContextMetainfoManager(configuration, facade, publisher);

        this.operationState = new ContainerState(configuration, facade, metainfoManager, publisher);
    }

    public void initializeContainerByDefault() {
        executeOperations(
                new ModuleFinderOperation(),
                new PreloadContextOperation(Constants.Contexts.INFRASTRUCTURE_CONTEXT),
                new PreloadContextOperation(Constants.Contexts.EXTERNAL_BEANS_CONTEXT),
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

        Object searchResult = operationState.getContainerContextFacade().getBean(properties);

        return BeanExtractor.isResolved(searchResult) ? (R) searchResult : null;
    }

    @SuppressWarnings("unchecked")
    public <R, C extends Collection<?>> R getBean(Class<?> beanClass, Class<C> collectionClass) {
        ResolvingProperties properties = ResolvingProperties
                .properties(beanClass)
                .collectionClass(collectionClass);

        Object searchResult = operationState.getContainerContextFacade().getBean(properties);

        return BeanExtractor.isResolved(searchResult) ? (R) searchResult : null;
    }

    @SuppressWarnings("unchecked")
    public <R> R getBean(String contextName, Class<?> beanClass) {
        return (R) operationState.getContainerContextFacade().getBean(ResolvingProperties
                .properties(beanClass)
                .beanContextSource(contextName)
        );
    }

    public <B> B autowire(B bean) {
        return null; //TODO реализовать оборачивание бина
    }

    public void stop() {
        operationState.getContainerContextFacade().stop();
    }
}
