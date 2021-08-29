package ru.jengine.battlemodule.core;

import ru.jengine.battlemodule.core.commandmaster.CommandsOnPhaseRegistrar;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.core.state.BattleState;

public class BattleContext {
    private final String battleId;
    private final BattleState battleState;
    private final BattleDynamicObjectsManager battleDynamicObjectsManager;
    private final CommandsOnPhaseRegistrar commandsOnPhaseRegistrar;
    private final DispatcherBattleWrapper dispatcher;

    public BattleContext(String battleId, BattleState battleState,
            BattleDynamicObjectsManager battleDynamicObjectsManager,
            CommandsOnPhaseRegistrar commandsOnPhaseRegistrar, DispatcherBattleWrapper dispatcher) {
        this.battleId = battleId;
        this.battleState = battleState;
        this.battleDynamicObjectsManager = battleDynamicObjectsManager;
        this.commandsOnPhaseRegistrar = commandsOnPhaseRegistrar;
        this.dispatcher = dispatcher;
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
}
