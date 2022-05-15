package ru.jengine.battlemodule.standardfilling.visible.outside;

import java.util.List;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.visible.UpdatableVisionInformationService;
import ru.jengine.eventqueue.event.PostHandler;

public interface VisionPostHandlerRegistrar {
    List<PostHandler<? extends BattleEvent>> getRegisteredPostHandlers(BattleState battleState,
            UpdatableVisionInformationService visionInformationService);
}
