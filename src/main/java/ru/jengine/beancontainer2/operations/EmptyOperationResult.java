package ru.jengine.beancontainer2.operations;

public class EmptyOperationResult implements OperationResult {
    public static final EmptyOperationResult INSTANCE = new EmptyOperationResult();

    private EmptyOperationResult() { }
}
