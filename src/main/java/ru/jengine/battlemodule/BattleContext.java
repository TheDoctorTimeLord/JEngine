package ru.jengine.battlemodule;

import ru.jengine.battlemodule.commandmaster.CommandsOnPhaseRegistrar;
import ru.jengine.battlemodule.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.state.BattleMapService;
import ru.jengine.battlemodule.state.BattleState;
import ru.jengine.eventqueue.Dispatcher;

public class BattleContext {
    private final BattleState battleState;
    private final BattleDynamicObjectsManager battleDynamicObjectsManager;
    private final CommandsOnPhaseRegistrar commandsOnPhaseRegistrar;
    private final BattleMapService battleMapService; //TODO избавиться
    private final Dispatcher dispatcher;

    public BattleContext(BattleState battleState, BattleDynamicObjectsManager battleDynamicObjectsManager,
            CommandsOnPhaseRegistrar commandsOnPhaseRegistrar, BattleMapService battleMapService,
            Dispatcher dispatcher) {
        this.battleState = battleState;
        this.battleDynamicObjectsManager = battleDynamicObjectsManager;
        this.commandsOnPhaseRegistrar = commandsOnPhaseRegistrar;
        this.battleMapService = battleMapService;
        this.dispatcher = dispatcher;
    }

    public BattleDynamicObjectsManager getBattleDynamicObjectsManager() {
        return battleDynamicObjectsManager;
    }

    public BattleMapService getBattleMapService() {
        return battleMapService;
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
