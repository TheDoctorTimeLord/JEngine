package ru.jengine.battlemodule.standardfilling.movement;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.utils.Logger;

@BattleBeanPrototype
public class MovementRegistrar extends AbstractContentRegistrar {
    private final Logger logger;

    public MovementRegistrar(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void registerInt() {
        registerPostHandler(new MovementHandler(battleContext.getBattleState(), logger));
    }
}
