package ru.jengine.utils;

public interface Logger {
    void log(String prefix, String message);
    void debug(String prefix, String message);

    void error(String prefix, String message);
    void error(String prefix, String message, Throwable throwable);
    void error(String prefix, Throwable throwable);
}
