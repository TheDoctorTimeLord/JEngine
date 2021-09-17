package ru.jengine.battlemodule.core.battlepresenter;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.scheduler.BattleSchedulerInitializer;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskRegistrar;
import ru.jengine.taskscheduler.ReusableTask;

@BattleBeanPrototype
public class ActionLoggerOnTurnPartBinder implements BattleSchedulerInitializer {
    @Override
    public void initializeScheduler(BattleContext battleContext) {
        BattleActionRegistrar actionRegistrar = battleContext.getBattleActionRegistrar();
        SchedulerTaskRegistrar taskRegistrar = battleContext.getTaskRegistrar();

        taskRegistrar.addTaskAfterPhase((ReusableTask)actionRegistrar::declareNewPhase);
        taskRegistrar.addTaskAfterTurn((ReusableTask)actionRegistrar::declareNewTurn);
    }
}
