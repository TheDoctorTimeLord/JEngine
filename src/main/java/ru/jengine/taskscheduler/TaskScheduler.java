package ru.jengine.taskscheduler;

public interface TaskScheduler {
    void addTask(String queueCode, Task task);
    void executeTaskQueue(String queueCode);
}
