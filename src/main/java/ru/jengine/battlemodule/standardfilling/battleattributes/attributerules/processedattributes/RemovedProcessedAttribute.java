package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRule;

/**
 * Атрибут, находящийся в процессе обработки, который был удалён
 */
public class RemovedProcessedAttribute extends AbstractProcessedAttribute {
    public RemovedProcessedAttribute(BattleAttribute attribute) {
        super(attribute);
    }

    @Override
    public List<AbstractProcessedAttribute> processAttribute(AttributeRule attributeRule, BattleModel model) {
        return attributeRule.processRemovedAttribute(model, attribute);
    }
}
