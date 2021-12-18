package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.serviceclasses.Direction;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.movement.CanMoved;
import ru.jengine.eventqueue.event.PostHandler;

public abstract class AbstractChangeDirectionHandler<E extends TestChangeDirectionEvent> implements PostHandler<E> {
    private final BattleState battleState;

    public AbstractChangeDirectionHandler(BattleState battleState) {
        this.battleState = battleState;
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
