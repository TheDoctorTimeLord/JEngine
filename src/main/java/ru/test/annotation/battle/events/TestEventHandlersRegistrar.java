package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.state.BattleState;

@BattleBeanPrototype
public class TestEventHandlersRegistrar extends AbstractContentRegistrar {
    @Override
    protected void registerInt() {
        BattleState state = battleContext.getBattleState();

        registerPostHandler(new ChangeDirectionHandler(state));
        registerPostHandler(new TestHitHandler(state, battleContext.getTaskRegistrar(),
                battleContext.getBattleDynamicObjectsManager()));

        registerPostHandler(new MoveBattleActionNotifier(battleContext.getBattleActionRegistrar()));
        registerPostHandler(new TestHitNotifier(battleContext.getBattleActionRegistrar()));
    }
}
