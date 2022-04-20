package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий в качестве информации строку
 */
public class StringAttribute extends BattleAttribute {
    private String value;

    public StringAttribute(String code, String value) {
        super(code);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public StringAttribute setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public BattleAttribute clone() {
        return new StringAttribute(getCode(), getValue()).setValue(getValue());
    }

    @Override
    public String toString() {
        return "StringAttribute [" + getCode() + "]=[" + value + "]";
    }
}
