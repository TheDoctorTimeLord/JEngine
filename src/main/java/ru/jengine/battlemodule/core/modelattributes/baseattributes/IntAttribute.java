package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий в качестве информации целое число
 */
public class IntAttribute extends BattleAttribute {
    private int value;

    public IntAttribute(String code, int value) {
        super(code);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public IntAttribute setValue(int value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "IntAttribute [" + getCode() + "]=[" + value + "]";
    }
}
