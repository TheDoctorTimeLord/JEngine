package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.eventqueue.event.PostHandler;
import ru.test.annotation.battle.battleactions.RotateAction;

public class TestChangeDirectionNotifier implements PostHandler<TestChangeDirectionEvent> {
    private final BattleActionRegistrar actionRegistrar;

    public TestChangeDirectionNotifier(BattleActionRegistrar actionRegistrar) {
        this.actionRegistrar = actionRegistrar;
    }

    @Override
    public Class<TestChangeDirectionEvent> getHandlingEventType() {
        return TestChangeDirectionEvent.class;
    }

    @Override
    public void handle(TestChangeDirectionEvent event) {
        actionRegistrar.registerAction(new RotateAction(event.getModelId(), event.getNewDirection()));
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}
