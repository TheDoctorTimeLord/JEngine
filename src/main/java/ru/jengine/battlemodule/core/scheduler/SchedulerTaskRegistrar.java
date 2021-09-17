package ru.jengine.battlemodule.core.scheduler;

import ru.jengine.taskscheduler.Task;

public interface SchedulerTaskRegistrar {
    void addTaskAfterPhase(Task task);
    void addTaskAfterTurn(Task task);
}
