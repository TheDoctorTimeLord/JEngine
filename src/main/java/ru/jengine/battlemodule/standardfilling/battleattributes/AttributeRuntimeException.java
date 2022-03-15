package ru.jengine.battlemodule.standardfilling.battleattributes;

public class AttributeRuntimeException extends RuntimeException {
    public AttributeRuntimeException(String message) {
        super(message);
    }

    public AttributeRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
