package ru.jengine.beancontainer.operations;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.exceptions.ContainerOperationException;

public class OperationsExecutor {
    private final ContainerOperation[] operationChain;
    private final ContainerState containerState;

    public OperationsExecutor(ContainerState containerState, ContainerOperation... operationChain) {
        this.containerState = containerState;
        this.operationChain = operationChain;
    }

    public void runOperationChain() {
        ContainerOperation lastOperation = null;
        OperationResult operationResult = new OperationResult();

        for (ContainerOperation operation : operationChain) {
            try {
                operation.apply(operationResult, containerState);
                lastOperation = operation;
            } catch (ContainerOperationException e) {
                String message = e.getMessage() + ". Previous operations [%s]"
                        .formatted(lastOperation == null ? null : lastOperation.getClass());
                throw new ContainerException(message, e);
            } catch (Exception e) {
                throw new ContainerException("Operation [%s] throws exception".formatted(operation.getClass()), e);
            }
        }
    }
}
