package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.handlingconditions.HandlingCondition;

/**
 * Реализация {@link AttributeHandlersFinder} по умолчанию
 * TODO можно оптимизировать некоторые случаи
 */
@BattleBeanPrototype
public class AttributeHandlersFinderDefault implements AttributeHandlersFinder {
    private final Map<HandlingCondition, AttributeHandler> attributeHandlersByCondition = new HashMap<>();

    public AttributeHandlersFinderDefault(List<AttributeHandler> attributeHandlers) {
        for (AttributeHandler handler : attributeHandlers) {
            for (HandlingCondition condition : handler.getHandledAttributeCodes()) {
                attributeHandlersByCondition.put(condition, handler);
            }
        }
    }

    @Override
    public List<AttributeHandler> findAttributeHandlers(BattleAttribute changedAttribute) {
        List<AttributeHandler> result = new ArrayList<>();

        for (Map.Entry<HandlingCondition, AttributeHandler> entry : attributeHandlersByCondition.entrySet()) {
            if (entry.getKey().canHandle(changedAttribute)) {
                result.add(entry.getValue());
            }
        }

        return result;
    }
}
