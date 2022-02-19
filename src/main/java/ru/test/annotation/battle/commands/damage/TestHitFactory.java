package ru.test.annotation.battle.commands.damage;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommandFactory;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.beancontainer.annotations.Bean;
import ru.test.annotation.battle.model.HasHealth;

@Bean
public class TestHitFactory implements BattleCommandFactory<NoneParameters, TestHit> {
    @Override
    public boolean canExecute(BattleModel model, BattleContext battleContext) {
        return model instanceof HasHealth && model instanceof CanMoved;
    }

    @Override
    public boolean isAvailableCommand(BattleModel model, BattleContext battleContext) {
        CanMoved canMoved = CanMoved.castToCanMoved(model);
        if (canMoved == null) {
            return false;
        }

        Point nextPosition = canMoved.nextPosition();
        return !battleContext.getBattleState().getOnPosition(nextPosition).isEmpty();
    }

    @Override
    public TestHit createBattleCommand(BattleModel model, BattleContext battleContext) {
        return new TestHit(((CanMoved)model).nextPosition());
    }
}
