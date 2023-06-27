package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.battlepresenter.BattleActionRegistrar;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.eventqueue.event.PostHandler;
import ru.test.annotation.battle.battleactions.HitBattleAction;

public class TestHitNotifier implements PostHandler<TestHitEvent> {
    private final BattleActionRegistrar actionRegistrar;

    public TestHitNotifier(BattleActionRegistrar actionRegistrar) {
        this.actionRegistrar = actionRegistrar;
    }

    @Override
    public void handle(TestHitEvent event) {
        actionRegistrar.registerAction(new HitBattleAction(event.getAttacker(), event.getTarget(), event.getDamagePoint()));
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}
