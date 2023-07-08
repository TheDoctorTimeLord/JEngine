package ru.jengine.utils.hierarchywalker;

public class WalkingException extends RuntimeException {
    private final Qualification exceptionQualification;

    public WalkingException(String message) {
        super(message);
        this.exceptionQualification = Qualification.DEFAULT;
    }

    public WalkingException(String message, Throwable throwable) {
        super(message, throwable);
        this.exceptionQualification = Qualification.DEFAULT;
    }

    public WalkingException(Qualification qualification) {
        this.exceptionQualification = qualification;
    }

    public Qualification getExceptionQualification() {
        return exceptionQualification;
    }

    enum Qualification {
        DEFAULT, MAPPER_NOT_FOUND, UNKNOWN_TYPE;
    }
}
