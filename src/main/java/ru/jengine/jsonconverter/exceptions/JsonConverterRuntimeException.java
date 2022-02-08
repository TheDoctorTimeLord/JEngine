package ru.jengine.jsonconverter.exceptions;

public class JsonConverterRuntimeException extends RuntimeException {
    public JsonConverterRuntimeException(String message) {
        super(message);
    }

    public JsonConverterRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
