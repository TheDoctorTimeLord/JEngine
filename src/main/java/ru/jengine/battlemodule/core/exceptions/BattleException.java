package ru.jengine.battlemodule.core.exceptions;

/**
 * Исключение, происходящее в бою
 */
public class BattleException extends RuntimeException {
    public BattleException(String message) {
        super(message);
    }

    public BattleException(String message, Throwable cause) {
        super(message, cause);
    }
}
