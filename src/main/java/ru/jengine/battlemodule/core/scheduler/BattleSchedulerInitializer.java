package ru.jengine.battlemodule.core.scheduler;

import ru.jengine.battlemodule.core.BattleContext;

public interface BattleSchedulerInitializer {
    void initializeScheduler(BattleContext battleContext);
}
