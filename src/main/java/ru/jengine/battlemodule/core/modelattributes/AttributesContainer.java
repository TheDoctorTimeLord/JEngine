package ru.jengine.battlemodule.core.modelattributes;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Контейнер, хранящий все атрибуты некоторой модели в бою
 */
public class AttributesContainer implements AttributeStore<AttributesContainer> {
    private final Map<String, BattleAttribute> attributes = new ConcurrentHashMap<>();

    @Override
    public <T extends BattleAttribute> T get(String attributeCode) {
        return (T)attributes.get(attributeCode);
    }

    @Override
    public AttributesContainer add(BattleAttribute attribute) {
        attributes.put(attribute.getCode(), attribute);
        return this;
    }

    @Override
    public BattleAttribute remove(String attributeCode) {
        return attributes.remove(attributeCode);
    }

    @Override
    public void removeAll() {
        attributes.clear();
    }

    @Override
    public Collection<BattleAttribute> getAttributes() {
        return attributes.values();
    }
}
