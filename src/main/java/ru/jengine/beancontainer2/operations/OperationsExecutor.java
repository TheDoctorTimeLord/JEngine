package ru.jengine.beancontainer2.operations;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.exceptions.ContainerException;

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
            } catch (ContainerException e) {
                if (ContainerOperation.CHECK_EXCEPTION_CODE.equals(e.getSpecialCode())) {
                    String message = e.getMessage() + ". Previous operations [%s]"
                            .formatted(lastOperation == null ? null : lastOperation.getClass());
                    throw new ContainerException(message, e);
                }

                throwUnknownException(operation, e);
            } catch (Exception e) {
                throwUnknownException(operation, e);
            }
        }
    }

    private void throwUnknownException(ContainerOperation operation, Throwable cause) {
        throw new ContainerException("Operation [%s] throws exception".formatted(operation.getClass()), cause);
    }
}
