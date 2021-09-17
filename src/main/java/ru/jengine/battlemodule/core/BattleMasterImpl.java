package ru.jengine.battlemodule.core;

import java.util.Collections;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionLogger;
import ru.jengine.battlemodule.core.battlepresenter.BattleActionPresenter;
import ru.jengine.battlemodule.core.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.core.commandmaster.BattleCommandMaster;
import ru.jengine.battlemodule.core.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.core.events.BattleEventPoolHandler;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.events.EventHandlerBinderService;
import ru.jengine.battlemodule.core.information.InformationCenter;
import ru.jengine.battlemodule.core.scheduler.BattleScheduler;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.core.state.BattleState;

@BattleBeanPrototype
public class BattleMasterImpl implements BattleMaster {
    private final IdGenerator idGenerator;
    private final BattleCommandMaster battleCommandMaster;
    private final EventHandlerBinderService eventHandlerBinderService;
    private final InformationCenter informationCenter;
    private final DispatcherBattleWrapper dispatcher;
    private final BattleScheduler battleScheduler;
    private final BattleActionLogger battleActionLogger;

    private final String battleId;

    private BattleContext context;

    public BattleMasterImpl(IdGenerator idGenerator, BattleCommandMaster battleCommandMaster,
            EventHandlerBinderService eventHandlerBinderService, InformationCenter informationCenter,
            DispatcherBattleWrapper dispatcher, BattleScheduler battleScheduler, BattleActionLogger battleActionLogger)
    {
        this.idGenerator = idGenerator;
        this.battleCommandMaster = battleCommandMaster;
        this.eventHandlerBinderService = eventHandlerBinderService;
        this.informationCenter = informationCenter;
        this.dispatcher = dispatcher;
        this.battleScheduler = battleScheduler;
        this.battleActionLogger = battleActionLogger;

        this.battleId = "battle" + idGenerator.generateId();
        this.dispatcher.setBattleId(this.battleId);
        this.battleScheduler.setBattleId(this.battleId);
    }

    public BattleContext getContext() {
        return context;
    }

    @Override
    public void prepareBattle(BattleGenerator battleGenerator, BattleCommandRegistrar commandRegistrar,
            BehaviorObjectsManager behaviorObjectsManager)
    {
        prepareDispatcherForBattle();

        battleGenerator.setIdGenerator(idGenerator);
        BattleState state = battleGenerator.generate();

        BattleDynamicObjectsManager dynamicObjectsManager = new BattleDynamicObjectsManager(state,
                behaviorObjectsManager);

        context = new BattleContext(battleId, state, dynamicObjectsManager, battleCommandMaster, dispatcher,
                battleScheduler, battleActionLogger);

        eventHandlerBinderService.bindPostHandlers(context);

        informationCenter.initializeByBattleContext(context);
        battleScheduler.initializeByBattleContext(context);

        dynamicObjectsManager.setCommandsForCharacters(commandRegistrar.getAllCommands(), context);
        behaviorObjectsManager.bindBehaviors(dynamicObjectsManager.getAllCharacters(), informationCenter);
    }

    @Override
    public void takeTurn() {
        battleCommandMaster.takeTurn(context, battleScheduler);
    }

    @Override
    public BattleActionPresenter getBattlePresenter() {
        return battleActionLogger;
    }

    private void prepareDispatcherForBattle() {
        BattleEventPoolHandler handler = new BattleEventPoolHandler();
        dispatcher.registerFastHandler(Collections.singletonList(handler), handler);
    }

    @Override
    public void stopBattle() {
        eventHandlerBinderService.unbindPostHandlers(dispatcher);
        dispatcher.removeFastHandler();
    }
}
