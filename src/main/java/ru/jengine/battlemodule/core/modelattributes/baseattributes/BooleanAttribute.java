package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий в качестве информации логический признак
 */
public class BooleanAttribute extends BattleAttribute {
    private boolean value;

    public BooleanAttribute(String code, boolean value) {
        super(code);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public BooleanAttribute setValue(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "FloatAttribute [" + getCode() + "]=[" + value + "]";
    }
}