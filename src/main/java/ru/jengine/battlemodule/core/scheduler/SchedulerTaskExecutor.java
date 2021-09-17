package ru.jengine.battlemodule.core.scheduler;

public interface SchedulerTaskExecutor {
    void executeBeforeTurn();
    void executeAfterPhase();
    void executeAfterTurn();
}
