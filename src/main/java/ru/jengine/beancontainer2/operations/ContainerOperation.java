package ru.jengine.beancontainer2.operations;

public interface ContainerOperation<C extends OperationResult> {
    OperationResult apply(C beforeOperationResult, ContainerOperationContext context);
}
