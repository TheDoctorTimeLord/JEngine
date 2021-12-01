package ru.test.annotation.battle.commands.rotate;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommandPrototype;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.beancontainer.annotations.Bean;
import ru.test.annotation.battle.model.HasHealth;

@Bean
public class TestRotatePrototype implements BattleCommandPrototype<RotateParameters, TestRotate> {
    @Override
    public boolean canExecute(BattleModel model, BattleContext battleContext) {
        return model instanceof CanMoved && ((CanMoved)model).hasPosition() && ((CanMoved)model).hasDirection();
    }

    @Override
    public boolean isAvailableCommand(BattleModel model, BattleContext battleContext) {
        if (model instanceof HasHealth && ((HasHealth)model).isDead()) {
            return false;
        }

        CanMoved canMoved = (CanMoved)model;
        return canMoved.hasPosition() && canMoved.hasDirection();
    }

    @Override
    public TestRotate createBattleCommand(BattleModel model, BattleContext battleContext) {
        return new TestRotate();
    }
}
