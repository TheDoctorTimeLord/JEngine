package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Условие, исходящее из кода атрибута. Если код атрибута совпадает с заданным, то такой атрибут можно обработать
 */
public class CodeCondition implements HandlingCondition {
    private final String expectedCode;

    public CodeCondition(String expectedCode) {
        this.expectedCode = expectedCode;
    }

    @Override
    public boolean canHandle(BattleAttribute attribute) {
        return expectedCode.equals(attribute.getCode());
    }

    public String getExpectedCode() {
        return expectedCode;
    }
}
