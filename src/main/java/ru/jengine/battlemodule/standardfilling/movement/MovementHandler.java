package ru.jengine.battlemodule.standardfilling.movement;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.eventqueue.event.PostHandler;

public class MovementHandler implements PostHandler<MoveEvent> {
    private final BattleState battleState;

    public MovementHandler(BattleState battleState) {
        this.battleState = battleState;
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.HANDLE.getPriority();
    }

    @Override
    public Class<MoveEvent> getHandlingEventType() {
        return MoveEvent.class;
    }

    @Override
    public void handle(MoveEvent event) {
        int id = event.getModelId();

        BattleModel model = battleState.resolveId(id);
        HasPosition moved = HasPosition.castToHasPosition(model);
        if (moved == null) {
            return; //TODO залогировать
        }
        moved.setPosition(event.getNewPosition());

        battleState.removeFromPosition(event.getOldPosition(), id);
        battleState.setToPosition(event.getNewPosition(), id);
    }
}
