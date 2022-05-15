package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions.CodeCondition;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions.HandlingCondition;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Реализация {@link AttributeRulesFinder} по умолчанию
 */
@BattleBeanPrototype
public class AttributeRulesFinderDefault implements AttributeRulesFinder {
    private final Map<HandlingCondition, AttributeRule> attributeHandlersByCondition = new HashMap<>();
    private final Multimap<String, CodeCondition> codeConditionMap = HashMultimap.create();
    private final Set<HandlingCondition> nonCodeConditions = new HashSet<>();

    public AttributeRulesFinderDefault(List<AttributeRule> attributeRules) {
        for (AttributeRule handler : attributeRules) {
            for (HandlingCondition condition : handler.getHandledAttributeCodes()) {
                attributeHandlersByCondition.put(condition, handler);

                if (condition instanceof CodeCondition codeCondition) {
                    codeConditionMap.put(codeCondition.getExpectedCode(), codeCondition);
                } else {
                    nonCodeConditions.add(condition);
                }
            }
        }
    }

    @Override
    public List<AttributeRule> findAttributeHandlers(BattleAttribute changedAttribute) {
        List<AttributeRule> result = new ArrayList<>();

        Collection<CodeCondition> codeConditions = codeConditionMap.get(changedAttribute.getCode());
        if (!codeConditions.isEmpty()) {
            for (CodeCondition condition : codeConditions) {
                if (condition.canHandle(changedAttribute)) {
                    result.add(attributeHandlersByCondition.get(condition));
                }
            }
        }

        for (HandlingCondition condition : nonCodeConditions) {
            if (condition.canHandle(changedAttribute)) {
                result.add(attributeHandlersByCondition.get(condition));
            }
        }

        return result;
    }
}
