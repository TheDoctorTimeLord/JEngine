package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesManager;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Главный обработчик события {@link PutAttributeNotification}
 */
public class PutAttributeNotificationHandler implements PostHandler<PutAttributeNotification> {
    private final AttributeRulesManager attributeRulesManager;
    private final BattleState battleState;

    public PutAttributeNotificationHandler(AttributeRulesManager attributeRulesManager, BattleState battleState) {
        this.attributeRulesManager = attributeRulesManager;
        this.battleState = battleState;
    }

    @Override
    public Class<PutAttributeNotification> getHandlingEventType() {
        return PutAttributeNotification.class;
    }

    @Override
    public void handle(PutAttributeNotification event) {
        BattleModel model = battleState.resolveId(event.getModelId());
        attributeRulesManager.processPuttedAttribute(model, event.getPuttedAttribute());
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}
