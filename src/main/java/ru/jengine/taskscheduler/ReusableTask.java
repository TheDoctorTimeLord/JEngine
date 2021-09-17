package ru.jengine.taskscheduler;

@FunctionalInterface
public interface ReusableTask extends Task {
    @Override
    default boolean isReusable() {
        return true;
    }
}
