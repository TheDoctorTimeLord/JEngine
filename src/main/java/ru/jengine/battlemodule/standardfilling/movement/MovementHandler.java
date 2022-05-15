package ru.jengine.battlemodule.standardfilling.movement;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.utils.Logger;

public class MovementHandler implements PostHandler<MoveEvent> {
    private final BattleState battleState;
    private final Logger logger;

    public MovementHandler(BattleState battleState, Logger logger) {
        this.battleState = battleState;
        this.logger = logger;
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
            logger.error("MovementHandler", "Model [%s] can not move".formatted(id));
            return;
        }
        moved.setPosition(event.getNewPosition());

        battleState.removeFromPosition(event.getOldPosition(), id);
        battleState.setToPosition(event.getNewPosition(), id);
    }
}
