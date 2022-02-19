package ru.test.annotation.battle.commands.move;

import java.util.HashSet;
import java.util.Set;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommandFactory;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.beancontainer.annotations.Bean;
import ru.test.annotation.battle.model.HasHealth;

@Bean
public class TestCommandMoveFactory implements BattleCommandFactory<MoveParameters, TestCommandMove> {
    @Override
    public boolean canExecute(BattleModel model, BattleContext battleContext) {
        return model instanceof CanMoved && ((CanMoved)model).hasPosition() && ((CanMoved)model).hasDirection();
    }

    @Override
    public boolean isAvailableCommand(BattleModel model, BattleContext battleContext) {
        if (model instanceof HasHealth && ((HasHealth)model).isDead()) {
            return false;
        }

        CanMoved canMoved = CanMoved.castToCanMoved(model);
        if (canMoved == null) {
            return false;
        }

        Point position = canMoved.getPosition();
        Direction direction = canMoved.getDirection();

        BattleState battleState = battleContext.getBattleState();
        return checkAvailablePosition(position, direction.rotateLeft(), battleState)
                || checkAvailablePosition(position, direction, battleState)
                || checkAvailablePosition(position, direction.rotateRight(), battleState);
    }

    private static boolean checkAvailablePosition(Point currentPosition, Direction direction, BattleState battleState) {
        return isAvailablePoint(currentPosition.add(direction.getOffset()), battleState);
    }

    public static boolean isAvailablePoint(Point checked, BattleState battleState) {
        return battleState.inBattlefieldBound(checked) && battleState.getOnPosition(checked).isEmpty();
    }

    @Override
    public TestCommandMove createBattleCommand(BattleModel model, BattleContext battleContext) {
        CanMoved canMoved = (CanMoved)model;
        Point position = canMoved.getPosition();
        Direction direction = canMoved.getDirection();

        Set<Point> options = new HashSet<>();
        Direction currentDirection = direction.rotateLeft();

        for (int i = 0; i < 3; i++) {
            Point option = position.add(currentDirection.getOffset());
            if (isAvailablePoint(option, battleContext.getBattleState())) {
                options.add(option);
            }
            currentDirection = currentDirection.rotateRight();
        }

        return new TestCommandMove(options);
    }
}
