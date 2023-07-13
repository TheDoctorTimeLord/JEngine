package ru.jengine.beancontainer.exceptions;

public class ContainerOperationException extends ContainerException {
    public ContainerOperationException(String message) {
        super(message);
    }

    public ContainerOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
