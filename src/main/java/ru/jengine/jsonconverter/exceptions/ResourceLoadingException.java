package ru.jengine.jsonconverter.exceptions;

public class ResourceLoadingException extends Exception {
    public ResourceLoadingException(String message) {
        super(message);
    }

    public ResourceLoadingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
