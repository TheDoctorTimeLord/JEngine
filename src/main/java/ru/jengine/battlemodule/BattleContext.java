package ru.jengine.battlemodule;

import ru.jengine.battlemodule.commandmaster.CommandsOnPhaseRegistrar;
import ru.jengine.battlemodule.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.state.BattleMapService;
import ru.jengine.battlemodule.state.BattleObjectsManager;
import ru.jengine.battlemodule.state.BattleState;
import ru.jengine.eventqueue.Dispatcher;

public class BattleContext {
    private final BattleState battleState;
    private final BattleDynamicObjectsManager battleDynamicObjectsManager;
    private final BattleObjectsManager battleObjectsManager;
    private final CommandsOnPhaseRegistrar commandsOnPhaseRegistrar;
    private final BattleMapService battleMapService;
    private final Dispatcher dispatcher;

    public BattleContext(BattleState battleState, BattleDynamicObjectsManager battleDynamicObjectsManager,
            BattleObjectsManager battleObjectsManager, CommandsOnPhaseRegistrar commandsOnPhaseRegistrar,
            BattleMapService battleMapService, Dispatcher dispatcher) {
        this.battleState = battleState;
        this.battleDynamicObjectsManager = battleDynamicObjectsManager;
        this.battleObjectsManager = battleObjectsManager;
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

    public BattleObjectsManager getBattleObjectsManager() {
        return battleObjectsManager;
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
