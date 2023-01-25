package ru.jengine.jsonconverter.exceptions;

public class JsonConverterException extends RuntimeException {
    public JsonConverterException(String message) {
        super(message);
    }

    public JsonConverterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
