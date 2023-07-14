package ru.jengine.beancontainer2;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.ContainerContextFacade;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.OperationsExecutor;
import ru.jengine.beancontainer2.statepublisher.ContainerStatePublisher;

public class JEngineContainer {
    private final ContainerState operationContext;

    public JEngineContainer(ContainerConfiguration configuration) {
        ContainerContextFacade facade = new ContainerContextFacade();
        ContextMetainfoManager metainfoManager = new ContextMetainfoManager(configuration, facade);
        ContainerStatePublisher containerStatePublisher = new ContainerStatePublisher();

        this.operationContext = new ContainerState(configuration, facade, metainfoManager, containerStatePublisher);
    }

    public void executeOperations(ContainerOperation<?>... operationsChain) {
        new OperationsExecutor(operationContext, operationsChain).runOperationChain();
    }

    public <R> R getBean(Class<?> beanClass) {
        return null; //TODO реализовать получение бина
    }

    public <R> R getBean(String beanName) {
        return null; //TODO реализовать получение по имени
    }

    public <R> R getBean(String contextName, Class<?> beanClass) {
        return null; //TODO реализовать получение по контексту
    }

    public <R> R getBean(String contextName, String beanName) {
        return null; //TODO реализовать получение из контекста по имени
    }

    public <B> B autowire(B bean) {
        return null; //TODO реализовать оборачивание бина
    }

    public void stop() {
        //TODO реализовать остановку
    }
}
