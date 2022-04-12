package ru.jengine.battlemodule.core.scheduler;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleIdSetter;
import ru.jengine.taskscheduler.Task;
import ru.jengine.taskscheduler.TaskScheduler;

/**
 * Обёртка для планировщика задач в игре. Предоставляет несколько специфичных для боевой системы категорий задач. Для
 * каждого боя создаётся своя обёртка планировщика задач
 */
@BattleBeanPrototype
public class BattleScheduler implements BattleIdSetter, SchedulerTaskRegistrar, SchedulerTaskExecutor {
    private final TaskScheduler scheduler;
    private String afterInitializeBattleQueue;
    private String beforeTurnTaskQueue;
    private String afterPhaseTaskQueue;
    private String afterTurnTaskQueue;

    public BattleScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void setBattleId(String battleId) {
        this.afterInitializeBattleQueue = battleId + "-afterInitializeBattle";
        this.beforeTurnTaskQueue = battleId + "-beforeTurn";
        this.afterPhaseTaskQueue = battleId + "-afterPhase";
        this.afterTurnTaskQueue = battleId + "-afterTurn";
    }

    @Override
    public void addTaskAfterInitializeBattle(Task task) {
        scheduler.addTask(afterInitializeBattleQueue, task);
    }

    @Override
    public void addTaskBeforeTurn(Task task) {
        scheduler.addTask(beforeTurnTaskQueue, task);
    }

    @Override
    public void addTaskAfterPhase(Task task) {
        scheduler.addTask(afterPhaseTaskQueue, task);
    }

    @Override
    public void addTaskAfterTurn(Task task) {
        scheduler.addTask(afterTurnTaskQueue, task);
    }

    @Override
    public void executeAfterInitializeBattle() {
        scheduler.executeTaskQueue(afterInitializeBattleQueue);
    }

    @Override
    public void executeBeforeTurn() {
        scheduler.executeTaskQueue(beforeTurnTaskQueue);
    }

    @Override
    public void executeAfterPhase() {
        scheduler.executeTaskQueue(afterPhaseTaskQueue);
    }

    @Override
    public void executeAfterTurn() {
        scheduler.executeTaskQueue(afterTurnTaskQueue);
    }
}
