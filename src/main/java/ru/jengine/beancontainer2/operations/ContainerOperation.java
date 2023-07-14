package ru.jengine.beancontainer2.operations;

import ru.jengine.beancontainer2.ContainerState;

public interface ContainerOperation<C extends OperationResult> {
    OperationResult apply(C beforeOperationResult, ContainerState context);
}
