package ru.jengine.battlemodule.standardfilling.movement;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;

@BattleBeanPrototype
public class MovementRegistrar extends AbstractContentRegistrar {

    @Override
    protected void registerInt() {
        registerPostHandler(new MovementHandler(battleContext.getBattleState()));
    }
}
