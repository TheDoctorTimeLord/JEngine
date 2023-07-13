package ru.jengine.beancontainer2;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.operations.ContainerOperation;
import ru.jengine.beancontainer2.operations.ContainerOperationContext;
import ru.jengine.beancontainer2.operations.OperationsExecutor;

public class JEngineContainer {
    private final ContainerOperationContext operationContext;

    public JEngineContainer(ContainerConfiguration configuration) {
        this.operationContext = new ContainerOperationContext(configuration);
    }

    public void executeOperations(ContainerOperation<?>... operationsChain) {
        new OperationsExecutor(operationContext, operationsChain).runOperationChain();
    }
}
