package ru.jengine.beancontainer2;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.BeanExtractor;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationsExecutor;
import ru.jengine.beancontainer2.operations.defaultimpl.*;
import ru.jengine.beancontainer2.statepublisher.ContainerStatePublisher;

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
    public <R> R getBean(Class<?> beanClass) {
        ResolvingProperties properties = ResolvingProperties
                .properties(beanClass)
                .collectionClass(Collection.class.isAssignableFrom(beanClass) ? beanClass : null);

        Object searchResult = operationState.getContainerContextFacade().getBean(properties);

        return searchResult != BeanExtractor.NOT_RESOLVED ? (R) searchResult : null;
    }

    public <R> R getBean(String beanName) {
        return null; //TODO реализовать получение по имени
    }

    @SuppressWarnings("unchecked")
    public <R> R getBean(String contextName, Class<?> beanClass) {
        return (R) operationState.getContainerContextFacade().getBean(ResolvingProperties
                .properties(beanClass)
                .beanContextSource(contextName)
        );
    }

    public <R> R getBean(String contextName, String beanName) {
        return null; //TODO реализовать получение из контекста по имени
    }

    public <B> B autowire(B bean) {
        return null; //TODO реализовать оборачивание бина
    }

    public void stop() {
        operationState.getContainerContextFacade().stop();
    }
}
