package ru.jengine.battlemodule.core.scheduler;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleIdSetter;
import ru.jengine.taskscheduler.Task;
import ru.jengine.taskscheduler.TaskScheduler;

@BattleBeanPrototype
public class BattleScheduler implements BattleIdSetter, SchedulerTaskRegistrar, SchedulerTaskExecutor {
    private final TaskScheduler scheduler;
    private String afterPhaseTaskQueue;

    public BattleScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void setBattleId(String battleId) {
        this.afterPhaseTaskQueue = battleId + "-afterPhase";
    }

    @Override
    public void addTaskAfterPhase(Task task) {
        scheduler.addTask(afterPhaseTaskQueue, task);
    }

    @Override
    public void executeAfterPhase() {
        scheduler.executeTaskQueue(afterPhaseTaskQueue);
    }
}
