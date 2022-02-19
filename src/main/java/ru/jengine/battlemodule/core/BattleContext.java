package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;
import ru.jengine.battlemodule.core.commandmaster.CommandsOnPhaseRegistrar;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskRegistrar;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.core.state.BattleState;

/**
 * Контекст конкретного боя. Предоставляет доступ к объектам, изменяющим состояние боя. Контекст используется для
 * внутренних компонент боевой системы
 */
public class BattleContext {
    private final String battleId;
    private final BattleState battleState;
    private final BattleDynamicObjectsManager battleDynamicObjectsManager;
    private final CommandsOnPhaseRegistrar commandsOnPhaseRegistrar;
    private final DispatcherBattleWrapper dispatcher;
    private final SchedulerTaskRegistrar taskRegistrar;
    private final BattleActionRegistrar battleActionRegistrar;

    public BattleContext(String battleId, BattleState battleState,
            BattleDynamicObjectsManager battleDynamicObjectsManager, CommandsOnPhaseRegistrar commandsOnPhaseRegistrar,
            DispatcherBattleWrapper dispatcher, SchedulerTaskRegistrar taskRegistrar,
            BattleActionRegistrar battleActionRegistrar) {
        this.battleId = battleId;
        this.battleState = battleState;
        this.battleDynamicObjectsManager = battleDynamicObjectsManager;
        this.commandsOnPhaseRegistrar = commandsOnPhaseRegistrar;
        this.dispatcher = dispatcher;
        this.taskRegistrar = taskRegistrar;
        this.battleActionRegistrar = battleActionRegistrar;
    }

    public BattleActionRegistrar getBattleActionRegistrar() {
        return battleActionRegistrar;
    }

    public BattleDynamicObjectsManager getBattleDynamicObjectsManager() {
        return battleDynamicObjectsManager;
    }

    public String getBattleId() {
        return battleId;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public CommandsOnPhaseRegistrar getCommandsOnPhaseRegistrar() {
        return commandsOnPhaseRegistrar;
    }

    public DispatcherBattleWrapper getDispatcher() {
        return dispatcher;
    }

    public SchedulerTaskRegistrar getTaskRegistrar() {
        return taskRegistrar;
    }
}
