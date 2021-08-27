package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.commandmaster.CommandsOnPhaseRegistrar;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.eventqueue.Dispatcher;

public class BattleContext {
    private final BattleState battleState;
    private final BattleDynamicObjectsManager battleDynamicObjectsManager;
    private final CommandsOnPhaseRegistrar commandsOnPhaseRegistrar;
    private final Dispatcher dispatcher;

    public BattleContext(BattleState battleState, BattleDynamicObjectsManager battleDynamicObjectsManager,
            CommandsOnPhaseRegistrar commandsOnPhaseRegistrar, Dispatcher dispatcher) {
        this.battleState = battleState;
        this.battleDynamicObjectsManager = battleDynamicObjectsManager;
        this.commandsOnPhaseRegistrar = commandsOnPhaseRegistrar;
        this.dispatcher = dispatcher;
    }

    public BattleDynamicObjectsManager getBattleDynamicObjectsManager() {
        return battleDynamicObjectsManager;
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public CommandsOnPhaseRegistrar getCommandsOnPhaseRegistrar() {
        return commandsOnPhaseRegistrar;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
