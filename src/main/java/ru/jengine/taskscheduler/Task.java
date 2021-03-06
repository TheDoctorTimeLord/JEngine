package ru.jengine.taskscheduler;

@FunctionalInterface
public interface Task {
    void execute();
    default boolean isReusable() {
        return false;
    };
}
