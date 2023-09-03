package ru.jengine.beancontainer.exceptions;

public class ContainerException extends RuntimeException {
    public static final String EMPTY_SPECIAL_CODE = "[empty]";
    private final String specialCode;

    public ContainerException(String message) {
        this(message, EMPTY_SPECIAL_CODE);
    }

    public ContainerException(String message, String specialCode) {
        super(message);
        this.specialCode = specialCode;
    }

    public ContainerException(String message, Throwable cause) {
        this(message, cause, EMPTY_SPECIAL_CODE);
    }

    public ContainerException(String message, Throwable cause, String specialCode) {
        super(message, cause);
        this.specialCode = specialCode;
    }

    public String getSpecialCode() {
        return specialCode;
    }
}
