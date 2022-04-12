package ru.jengine.battlemodule.core.containers;

/**
 * Исключение, связанное с работой с предметами внутри боя
 */
public class ItemRuntimeException extends RuntimeException {
    public ItemRuntimeException(String message) {
        super(message);
    }

    public ItemRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
