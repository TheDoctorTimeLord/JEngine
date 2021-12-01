package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.eventqueue.event.PostHandler;

public class ChangeDirectionHandler implements PostHandler<TestChangeDirectionEvent> {
    private final BattleState battleState;

    public ChangeDirectionHandler(BattleState battleState) {
        this.battleState = battleState;
    }

    @Override
    public Class<TestChangeDirectionEvent> getHandlingEventType() {
        return TestChangeDirectionEvent.class;
    }

    @Override
    public void handle(TestChangeDirectionEvent event) {
        Direction newDirection = event.getNewDirection();

        if (newDirection != null) {
            BattleModel battleModel = battleState.resolveId(event.getModelId());
            if (battleModel instanceof CanMoved) {
                ((CanMoved)battleModel).setDirection(newDirection);
            }
        }
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.HANDLE.getPriority();
    }
}
