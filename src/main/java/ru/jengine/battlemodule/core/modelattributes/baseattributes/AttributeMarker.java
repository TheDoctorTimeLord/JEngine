package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, не хранящий дополнительной информации
 */
public class AttributeMarker extends BattleAttribute {
    public AttributeMarker(String code) {
        super(code);
    }

    @Override
    public BattleAttribute clone() {
        return new AttributeMarker(getCode()).setPath(getPath());
    }

    @Override
    public String toString() {
        return "AttributeMarker [" + getCode() + "]";
    }
}
