package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.movement.MoveEvent;
import ru.jengine.eventqueue.event.PostHandler;
import ru.test.annotation.battle.battleactions.MoveBattleAction;

public class MoveBattleActionNotifier implements PostHandler<MoveEvent> {
    private final BattleActionRegistrar actionRegistrar;

    public MoveBattleActionNotifier(BattleActionRegistrar actionRegistrar) {
        this.actionRegistrar = actionRegistrar;
    }

    @Override
    public Class<MoveEvent> getHandlingEventType() {
        return MoveEvent.class;
    }

    @Override
    public void handle(MoveEvent event) {
        actionRegistrar.registerAction(new MoveBattleAction(event.getModelId(), event.getOldPosition(), event.getNewPosition()));
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}
