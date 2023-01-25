package ru.jengine.jsonconverter.exceptions;

public class JsonLoaderException extends Exception {
    public JsonLoaderException(String message) {
        super(message);
    }

    public JsonLoaderException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
