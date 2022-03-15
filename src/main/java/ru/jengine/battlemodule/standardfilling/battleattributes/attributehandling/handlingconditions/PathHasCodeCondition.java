package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.handlingconditions;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Условие, исходящее из кода атрибута. Если код атрибута совпадает с заданным или находится в пути атрибута, то такой
 * атрибут можно обработать
 */
public class PathHasCodeCondition extends CodeCondition {
    public PathHasCodeCondition(String expectedCode) {
        super(expectedCode);
    }

    @Override
    public boolean canHandle(BattleAttribute attribute) {
        return super.canHandle(attribute) || attribute.getPath().contains(getExpectedCode());
    }
}
