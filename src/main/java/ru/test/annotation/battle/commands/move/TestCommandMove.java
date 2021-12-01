package ru.test.annotation.battle.commands.move;

import static ru.test.annotation.battle.commands.move.TestCommandMovePrototype.isAvailablePoint;

import java.util.Set;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.commands.BattleCommand;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.serviceclasses.Point;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.battlemodule.standardfilling.movement.MoveEvent;
import ru.test.annotation.battle.events.TestChangeDirectionEvent;

public class TestCommandMove implements BattleCommand<MoveParameters> {
    private final Set<Point> availablePositions;

    public TestCommandMove(Set<Point> availablePositions) {
        this.availablePositions = availablePositions;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public MoveParameters createParametersTemplate() {
        return new MoveParameters(availablePositions);
    }

    @Override
    public void perform(BattleModel model, BattleContext battleContext, MoveParameters executionParameters) {
        BattleState battleState = battleContext.getBattleState();
        if (isExecutionParametersCorrect(executionParameters))
        {
            CanMoved canMoved = (CanMoved)model;
            Point oldPosition = canMoved.getPosition();
            Point newPosition = executionParameters.getAnswer();
            Direction newDirection = getChangedDirection(oldPosition, newPosition);

            DispatcherBattleWrapper dispatcher = battleContext.getDispatcher();

            if (isAvailablePoint(newPosition, battleState)) {
                dispatcher.handle(new MoveEvent(model.getId(), oldPosition, newPosition));
            }
            dispatcher.handle(new TestChangeDirectionEvent(model.getId(), newDirection));
        }
    }

    private static boolean isExecutionParametersCorrect(MoveParameters moveParameters) {
        Point answer = moveParameters.getAnswer();
        return moveParameters.getAvailablePoints().contains(answer);
    }

    private static Direction getChangedDirection(Point oldPosition, Point newPosition) {
        return Direction.getByOffset(newPosition.sub(oldPosition));
    }
}
