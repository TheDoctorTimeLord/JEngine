package ru.jengine.beancontainer2.exceptions;

public class InvocationMethodException extends RuntimeException {
    public InvocationMethodException(String message) {
        super(message);
    }

    public InvocationMethodException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
