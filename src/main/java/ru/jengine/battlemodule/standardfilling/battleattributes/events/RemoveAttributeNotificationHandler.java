package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesManager;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Главный обработчик события {@link RemoveAttributeNotification}
 */
public class RemoveAttributeNotificationHandler implements PostHandler<RemoveAttributeNotification> {
    private final AttributeRulesManager attributeRulesManager;
    private final BattleState battleState;

    public RemoveAttributeNotificationHandler(AttributeRulesManager attributeRulesManager, BattleState battleState) {
        this.attributeRulesManager = attributeRulesManager;
        this.battleState = battleState;
    }

    @Override
    public void handle(RemoveAttributeNotification event) {
        BattleModel model = battleState.resolveId(event.getModelId());
        attributeRulesManager.processRemovedAttribute(model, event.getRemovedAttribute());
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}