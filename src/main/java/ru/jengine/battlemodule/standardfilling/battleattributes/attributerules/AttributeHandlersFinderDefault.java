package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions.HandlingCondition;

/**
 * Реализация {@link AttributeHandlersFinder} по умолчанию
 * TODO можно оптимизировать некоторые случаи
 */
@BattleBeanPrototype
public class AttributeHandlersFinderDefault implements AttributeHandlersFinder {
    private final Map<HandlingCondition, AttributeRule> attributeHandlersByCondition = new HashMap<>();

    public AttributeHandlersFinderDefault(List<AttributeRule> attributeRules) {
        for (AttributeRule handler : attributeRules) {
            for (HandlingCondition condition : handler.getHandledAttributeCodes()) {
                attributeHandlersByCondition.put(condition, handler);
            }
        }
    }

    @Override
    public List<AttributeRule> findAttributeHandlers(BattleAttribute changedAttribute) {
        List<AttributeRule> result = new ArrayList<>();

        for (Map.Entry<HandlingCondition, AttributeRule> entry : attributeHandlersByCondition.entrySet()) {
            if (entry.getKey().canHandle(changedAttribute)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }
}
