package ru.jengine.taskscheduler;

public interface Task {
    void execute();
    default boolean isReusable() {
        return false;
    };
}
