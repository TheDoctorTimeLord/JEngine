package ru.jengine.battlemodule.core.modelattributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.battlemodule.core.modelattributes.baseattributes.AttributesBasedAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.BooleanAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.FloatAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.IntAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.StringAttribute;

/**
 * Контейнер, хранящий все атрибуты некоторой модели в бою
 */
public class AttributesContainer {
    private final Map<String, BattleAttribute> attributes = new ConcurrentHashMap<>();

    public <T extends BattleAttribute> T getAttribute(String attributeCode) {
        return (T)attributes.get(attributeCode);
    }

    public StringAttribute getAsString(String attributeCode) {
        return getAttribute(attributeCode);
    }

    public IntAttribute getAsInt(String attributeCode) {
        return getAttribute(attributeCode);
    }

    public FloatAttribute getAsFloat(String attributeCode) {
        return getAttribute(attributeCode);
    }

    public BooleanAttribute getAsBoolean(String attributeCode) {
        return getAttribute(attributeCode);
    }

    public AttributesBasedAttribute getAsContainer(String attributeCode) {
        return getAttribute(attributeCode);
    }

    public boolean containsAttribute(String attributeCode) {
        return attributes.containsKey(attributeCode);
    }

    public AttributesContainer addAttribute(BattleAttribute attribute) {
        attributes.put(attribute.getCode(), attribute);
        return this;
    }

    public BattleAttribute removeAttribute(String attributeCode) {
        return attributes.remove(attributeCode);
    }
}
