package ru.test.annotation.battle.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.scheduler.SchedulerTaskRegistrar;
import ru.jengine.battlemodule.core.state.BattleDynamicObjectsManager;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.eventqueue.event.PostHandler;
import ru.test.annotation.battle.model.HasHealth;

public class TestHitHandler implements PostHandler<TestHitEvent> {
    private final BattleState battleState;
    private final SchedulerTaskRegistrar taskRegistrar;
    private final BattleDynamicObjectsManager dynamicObjectsManager;

    public TestHitHandler(BattleState battleState, SchedulerTaskRegistrar taskRegistrar,
            BattleDynamicObjectsManager dynamicObjectsManager)
    {
        this.battleState = battleState;
        this.taskRegistrar = taskRegistrar;
        this.dynamicObjectsManager = dynamicObjectsManager;
    }

    @Override
    public Class<TestHitEvent> getHandlingEventType() {
        return TestHitEvent.class;
    }

    @Override
    public void handle(TestHitEvent event) {
        BattleModel model = battleState.resolveId(event.getTarget());
        if (model instanceof HasHealth) {
            HasHealth hasHealth = (HasHealth)model;
            hasHealth.damage(event.getDamagePoint());

            if (hasHealth.isDead()) {
                taskRegistrar.addTaskAfterTurn(() -> dynamicObjectsManager.removeCharacter(model.getId()));
            }
        }
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.HANDLE.getPriority();
    }
}
