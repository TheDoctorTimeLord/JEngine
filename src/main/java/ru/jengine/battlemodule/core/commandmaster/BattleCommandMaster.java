package ru.jengine.battlemodule.core.commandmaster;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskExecutor;

public interface BattleCommandMaster extends CommandsOnPhaseRegistrar {
    void takeTurn(BattleContext commandContext, SchedulerTaskExecutor taskExecutor);
}
