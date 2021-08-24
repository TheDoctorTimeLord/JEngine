package ru.jengine.battlemodule;

import ru.jengine.battlemodule.behaviors.BehaviorObjectsManager;
import ru.jengine.battlemodule.commandmaster.BattleCommandMaster;
import ru.jengine.battlemodule.commands.BattleCommandRegistrar;
import ru.jengine.battlemodule.events.EventHandlerBinderService;
import ru.jengine.battlemodule.information.InformationCenter;
import ru.jengine.battlemodule.information.informaionservices.InformationRegistrarService;
import ru.jengine.battlemodule.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.state.BattleMapService;
import ru.jengine.battlemodule.state.BattleState;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;
import ru.jengine.eventqueue.Dispatcher;

@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public class BattleMasterImpl implements BattleMaster {
    private final IdGenerator idGenerator;
    private final BattleCommandMaster battleCommandMaster;
    private final EventHandlerBinderService binderService;
    private final InformationRegistrarService informationRegistrarService;
    private final InformationCenter informationCenter;
    private final Dispatcher dispatcher;

    private BattleContext context;

    public BattleMasterImpl(IdGenerator idGenerator, BattleCommandMaster battleCommandMaster,
            EventHandlerBinderService binderService,
            InformationRegistrarService informationRegistrarService,
            InformationCenter informationCenter, Dispatcher dispatcher)
    {
        this.idGenerator = idGenerator;
        this.battleCommandMaster = battleCommandMaster;
        this.binderService = binderService;
        this.informationRegistrarService = informationRegistrarService;
        this.informationCenter = informationCenter;
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

        BattleDynamicObjectsManager dynamicObjectsManager = new BattleDynamicObjectsManager(state,
                behaviorObjectsManager);
        BattleMapService battleMapService = new BattleMapService(state);

        context = new BattleContext(state, dynamicObjectsManager, battleCommandMaster, battleMapService, dispatcher);

        binderService.bindPostHandlers(context);

        informationCenter.initializeByBattleContext(context);
        informationRegistrarService.registerInformationServices(informationCenter, context);

        dynamicObjectsManager.setCommandsForCharacters(commandRegistrar.getAllCommands(), context);
        behaviorObjectsManager.bindBehaviors(dynamicObjectsManager.getAllCharacters(), informationCenter);
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
