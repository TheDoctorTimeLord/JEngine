package ru.jengine.battlemodule.standardfilling.battleattributes;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeHandlerManager;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeHandlersFinder;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotificationHandler;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.RemoveAttributeNotificationHandler;

/**
 * Регистратор системы обработки изменённых атрибутов
 */
@BattleBeanPrototype
public class AttributeHandlerRegistrar extends AbstractContentRegistrar {
    private final AttributeHandlersFinder attributeHandlersFinder;

    public AttributeHandlerRegistrar(AttributeHandlersFinder attributeHandlersFinder) {
        this.attributeHandlersFinder = attributeHandlersFinder;
    }

    @Override
    protected void registerInt() {
        AttributeHandlerManager attributeHandlerManager = new AttributeHandlerManager(attributeHandlersFinder);
        BattleState state = battleContext.getBattleState();

        //TODO Объединить каким-то образом
        registerPostHandler(new PutAttributeNotificationHandler(attributeHandlerManager, state));
        registerPostHandler(new RemoveAttributeNotificationHandler(attributeHandlerManager, state));
    }
}
