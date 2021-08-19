package ru.jengine.battlemodule;

import ru.jengine.battlemodule.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.commandmaster.BattleCommandMaster;
import ru.jengine.battlemodule.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.events.EventHandlerBinderService;
import ru.jengine.battlemodule.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.state.BattleMapService;
import ru.jengine.battlemodule.state.BattleObjectsManager;
import ru.jengine.battlemodule.state.BattleState;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;
import ru.jengine.eventqueue.Dispatcher;

@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public class BattleMasterImpl implements BattleMaster {
    private final IdGenerator idGenerator;
    private final BattleCommandMaster battleCommandMaster;
    private final EventHandlerBinderService binderService;
    private final Dispatcher dispatcher;

    private BattleContext context;

    public BattleMasterImpl(IdGenerator idGenerator, BattleCommandMaster battleCommandMaster,
            EventHandlerBinderService binderService, Dispatcher dispatcher)
    {
        this.idGenerator = idGenerator;
        this.battleCommandMaster = battleCommandMaster;
        this.binderService = binderService;
        this.dispatcher = dispatcher;
    }

    public BattleContext getContext() {
        return context;
    }

    @Override
    public void prepareBattle(BattleGenerator battleGenerator, BattleCommandRegistrar commandRegistrar,
            BehaviorObjectsManager behaviorObjectsManager)
    {
        battleGenerator.setIdGenerator(idGenerator);
        BattleState state = battleGenerator.generate();

        BattleObjectsManager battleObjectsManager = new BattleObjectsManager(state);
        BattleDynamicObjectsManager dynamicObjectsManager = new BattleDynamicObjectsManager(state,
                behaviorObjectsManager);
        BattleMapService battleMapService = new BattleMapService(state, battleObjectsManager);

        context = new BattleContext(state, dynamicObjectsManager, battleObjectsManager, battleCommandMaster,
                battleMapService, dispatcher);

        binderService.bindPostHandlers(context);

        dynamicObjectsManager.setCommandsForCharacters(commandRegistrar.getAllCommands(), context);
        behaviorObjectsManager.bindBehaviors(dynamicObjectsManager.getAllCharacters(context));
    }

    @Override
    public void takeTurn() {
        battleCommandMaster.takeTurn(context);
    }

    @Override
    public void stopBattle() {
        binderService.unbindPostHandlers(dispatcher);
    }
}
