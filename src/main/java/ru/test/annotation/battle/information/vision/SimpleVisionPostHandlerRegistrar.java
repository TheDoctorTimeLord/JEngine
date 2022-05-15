package ru.test.annotation.battle.information.vision;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.movement.MoveEvent;
import ru.jengine.battlemodule.standardfilling.visible.UpdatableVisionInformationService;
import ru.jengine.battlemodule.standardfilling.visible.outside.VisionPostHandlerRegistrar;
import ru.jengine.eventqueue.event.PostHandler;

@BattleBeanPrototype
public class SimpleVisionPostHandlerRegistrar implements VisionPostHandlerRegistrar {
    @Override
    public List<PostHandler<? extends BattleEvent>> getRegisteredPostHandlers(BattleState battleState,
            UpdatableVisionInformationService visionInformationService)
    {
        return List.of(new RecalculateAfterMoveHandler(visionInformationService));
    }

    private static class RecalculateAfterMoveHandler implements PostHandler<MoveEvent> {
        private final UpdatableVisionInformationService visionInformationService;

        private RecalculateAfterMoveHandler(UpdatableVisionInformationService visionInformationService) {
            this.visionInformationService = visionInformationService;
        }

        @Override
        public Class<MoveEvent> getHandlingEventType() {
            return MoveEvent.class;
        }

        @Override
        public void handle(MoveEvent event) {
            visionInformationService.recalculateFieldOfView(event.getModelId());
        }

        @Override
        public int getPriority() {
            return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
        }
    }
}
