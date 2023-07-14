package ru.jengine.beancontainer2.operations;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.exceptions.ContainerException;

public class OperationsExecutor {
    private final ContainerOperation<?>[] operationChain;
    private final ContainerState containerState;

    public OperationsExecutor(ContainerState containerState, ContainerOperation<?>... operationChain) {
        this.containerState = containerState;
        this.operationChain = operationChain;
    }

    public void runOperationChain() {
        ContainerOperation<?> lastOperation = null;
        OperationResult lastOperationResult = EmptyOperationResult.INSTANCE;

        for (ContainerOperation<?> containerOperation : operationChain) {
            ContainerOperation<OperationResult> operation = (ContainerOperation<OperationResult>) containerOperation;

            try {
                lastOperationResult = operation.apply(lastOperationResult, containerState);
                lastOperation = operation;
            } catch (ClassCastException e) {
                String message = "Operation [%s] has unexpected 'beforeOperationResult' - [%s] from operation [%s]"
                        .formatted(
                                operation.getClass(),
                                lastOperationResult.getClass(),
                                lastOperation == null ? null : lastOperation.getClass()
                        );
                throw new ContainerException(message, e);
            } catch (Exception e) {
                throw new ContainerException("Operation [%s] throws exception".formatted(operation.getClass()), e);
            }
        }
    }
}
