package ru.jengine.battlemodule.standardfilling.visible;

import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.movement.MoveEvent;
import ru.jengine.eventqueue.event.PostHandler;

public class RecalculateAfterMoveHandler implements PostHandler<MoveEvent> {
    private final UpdatableVisionInformationService visionService;

    public RecalculateAfterMoveHandler(UpdatableVisionInformationService visionService) {
        this.visionService = visionService;
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }

    @Override
    public Class<MoveEvent> getHandlingEventType() {
        return MoveEvent.class;
    }

    @Override
    public void handle(MoveEvent event) {
        visionService.recalculateFieldOfView(event.getModelId());
    }
}
