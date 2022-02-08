package ru.jengine.jsonconverter.exceptions;

public class JsonConvertException extends Exception {
    public JsonConvertException(String message) {
        super(message);
    }

    public JsonConvertException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
