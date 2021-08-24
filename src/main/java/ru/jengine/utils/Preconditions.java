package ru.jengine.utils;

public class Preconditions {
    public static void checkState(boolean expr, String errorMessage) {
        if (!expr) {
            throw new AssertionException(errorMessage);
        }
    }

    public static class AssertionException extends RuntimeException {
        public AssertionException(String message) {
            super(message);
        }
    }
}
