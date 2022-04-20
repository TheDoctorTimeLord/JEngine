package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий в качестве информации число с плавающей запятой
 */
public class FloatAttribute extends BattleAttribute {
    private float value;

    public FloatAttribute(String code, float value) {
        super(code);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public FloatAttribute setValue(float value) {
        this.value = value;
        return this;
    }

    @Override
    public BattleAttribute clone() {
        return new FloatAttribute(getCode(), getValue()).setValue(getValue());
    }

    @Override
    public String toString() {
        return "FloatAttribute [" + getCode() + "]=[" + value + "]";
    }
}