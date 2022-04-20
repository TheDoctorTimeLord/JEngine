package ru.jengine.battlemodule.core.modelattributes;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Objects;

/**
 * Контейнер, хранящий все атрибуты некоторой модели в бою
 */
public class AttributesContainer implements AttributeStore<AttributesContainer>, Cloneable {
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

    @Override
    public AttributesContainer clone() {
        AttributesContainer clonedContainer = new AttributesContainer();

        for (BattleAttribute attribute : attributes.values()) {
            clonedContainer.add(attribute.clone());
        }

        return clonedContainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AttributesContainer that))
            return false;
        return Objects.equal(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attributes);
    }
}
