package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.processedattributes;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandler;

/**
 * Атрибут, находящийся в процессе обработки, который был удалён
 */
public class RemovedProcessedAttribute extends AbstractProcessedAttribute {
    public RemovedProcessedAttribute(BattleAttribute attribute) {
        super(attribute);
    }

    @Override
    public List<AbstractProcessedAttribute> processAttribute(AttributeHandler attributeHandler, BattleModel model) {
        return attributeHandler.processRemovedAttribute(model, attribute);
    }
}
