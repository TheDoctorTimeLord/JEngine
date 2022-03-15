package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandlerManager;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Главный обработчик события {@link RemoveAttributeNotification}
 */
public class RemoveAttributeNotificationHandler implements PostHandler<RemoveAttributeNotification> {
    private final AttributeHandlerManager attributeHandlerManager;
    private final BattleState battleState;

    public RemoveAttributeNotificationHandler(AttributeHandlerManager attributeHandlerManager, BattleState battleState) {
        this.attributeHandlerManager = attributeHandlerManager;
        this.battleState = battleState;
    }

    @Override
    public Class<RemoveAttributeNotification> getHandlingEventType() {
        return RemoveAttributeNotification.class;
    }

    @Override
    public void handle(RemoveAttributeNotification event) {
        BattleModel model = battleState.resolveId(event.getModelId());
        attributeHandlerManager.processRemovedAttribute(model, event.getRemovedAttribute());
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}