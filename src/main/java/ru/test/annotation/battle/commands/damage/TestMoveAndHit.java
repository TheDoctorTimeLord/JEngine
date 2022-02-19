package ru.test.annotation.battle.commands.damage;

import static ru.test.annotation.battle.commands.move.TestCommandMoveFactory.inBound;
import static ru.test.annotation.battle.commands.move.TestCommandMoveFactory.isAvailablePoint;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.commands.executioncontexts.NoneParameters;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.battlemodule.standardfilling.movement.MoveEvent;
import ru.jengine.beancontainer.annotations.Bean;
import ru.test.annotation.battle.commands.NoParametersCommand;
import ru.test.annotation.battle.model.HasHealth;

@Bean
public class TestMoveAndHit extends NoParametersCommand {
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean canExecute(BattleModel model, BattleContext battleContext) {
        return CanMoved.castToCanMoved(model) != null && model instanceof HasHealth;
    }

    @Override
    public boolean isAvailableCommand(BattleModel model, BattleContext battleContext) {
        CanMoved canMoved = (CanMoved)model;
        HasHealth hasHealth = (HasHealth)model;

        if (hasHealth.isDead()) {
            return false;
        }

        BattleState battleState = battleContext.getBattleState();
        Point nextPosition = canMoved.nextPosition();
        Point afterNextPosition = nextPosition.add(canMoved.getDirection().getOffset());

        return isAvailablePoint(nextPosition, battleState) && inBound(afterNextPosition) &&
                !battleState.getOnPosition(afterNextPosition).isEmpty();
    }

    @Override
    public BattleCommand<NoneParameters> createBattleCommand(BattleModel model, BattleContext battleContext) {
        return new TestMoveAndHit();
    }

    @Override
    protected void perform(BattleModel model, BattleContext battleContext) {
        CanMoved canMoved = (CanMoved)model;
        Point oldPosition = canMoved.getPosition();
        Point nextPosition = canMoved.nextPosition();
        BattleState battleState = battleContext.getBattleState();

        if (!isAvailablePoint(nextPosition, battleState)) {
            return;
        }

        battleContext.getDispatcher().handle(new MoveEvent(model.getId(), oldPosition, nextPosition));

        Point afterNextPosition = nextPosition.add(canMoved.getDirection().getOffset());
        battleContext.getCommandsOnPhaseRegistrar()
                .registerCommandOnNextPhase(model.getId(), new TestHit(afterNextPosition));
    }
}
