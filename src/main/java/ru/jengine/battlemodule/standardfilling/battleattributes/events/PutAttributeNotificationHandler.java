package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandlerManager;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Главный обработчик события {@link PutAttributeNotification}
 */
public class PutAttributeNotificationHandler implements PostHandler<PutAttributeNotification> {
    private final AttributeHandlerManager attributeHandlerManager;
    private final BattleState battleState;

    public PutAttributeNotificationHandler(AttributeHandlerManager attributeHandlerManager, BattleState battleState) {
        this.attributeHandlerManager = attributeHandlerManager;
        this.battleState = battleState;
    }

    @Override
    public Class<PutAttributeNotification> getHandlingEventType() {
        return PutAttributeNotification.class;
    }

    @Override
    public void handle(PutAttributeNotification event) {
        BattleModel model = battleState.resolveId(event.getModelId());
        attributeHandlerManager.processPuttedAttribute(model, event.getPuttedAttribute());
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.RESULT_INFORMATION.getPriority();
    }
}
