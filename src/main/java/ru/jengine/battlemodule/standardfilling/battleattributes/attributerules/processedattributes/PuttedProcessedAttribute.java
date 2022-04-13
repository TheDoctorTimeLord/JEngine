package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRule;

/**
 * Атрибут, находящийся в процессе обработки, который был добавлен или изменён
 */
public class PuttedProcessedAttribute extends AbstractProcessedAttribute {
    public PuttedProcessedAttribute(BattleAttribute attribute) {
        super(attribute);
    }

    @Override
    public List<AbstractProcessedAttribute> processAttribute(AttributeRule attributeRule, BattleModel model) {
        return attributeRule.processPuttedAttribute(model, attribute);
    }
}
