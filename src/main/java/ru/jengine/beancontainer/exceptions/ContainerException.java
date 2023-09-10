package ru.jengine.beancontainer.exceptions;

public class ContainerException extends RuntimeException {
    public ContainerException(String message) {
        super(message);
    }

    public ContainerException(String message, Throwable cause) {
        super(message, cause);
    }
}
