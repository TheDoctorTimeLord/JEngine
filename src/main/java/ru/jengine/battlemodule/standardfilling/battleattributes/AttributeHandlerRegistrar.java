package ru.jengine.battlemodule.standardfilling.battleattributes;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesManager;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesFinder;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotificationHandler;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.RemoveAttributeNotificationHandler;

/**
 * Регистратор системы обработки изменённых атрибутов
 */
@BattleBeanPrototype
public class AttributeHandlerRegistrar extends AbstractContentRegistrar {
    private final AttributeRulesFinder attributeRulesFinder;

    public AttributeHandlerRegistrar(AttributeRulesFinder attributeRulesFinder) {
        this.attributeRulesFinder = attributeRulesFinder;
    }

    @Override
    protected void registerInt() {
        AttributeRulesManager attributeRulesManager = new AttributeRulesManager(attributeRulesFinder, battleContext.getDispatcher());
        BattleState state = battleContext.getBattleState();

        //TODO Объединить каким-то образом
        registerPostHandler(new PutAttributeNotificationHandler(attributeRulesManager, state));
        registerPostHandler(new RemoveAttributeNotificationHandler(attributeRulesManager, state));
    }
}
