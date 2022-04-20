package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.modelattributes.AttributeStore;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.utils.CollectionUtils;

/**
 * Атрибут, хранящий в качестве дополнительной информации другие атрибуты
 */
public class AttributesBasedAttribute extends BattleAttribute implements AttributeStore<AttributesBasedAttribute> {
    private final Map<String, BattleAttribute> attributes = new HashMap<>();

    public AttributesBasedAttribute(String code) {
        super(code);
    }

    @Override
    public <T extends BattleAttribute> T get(String attributeCode) {
        return (T)attributes.get(attributeCode);
    }

    @Override
    public AttributesBasedAttribute add(BattleAttribute attribute) {
        List<String> pathToAddedAttribute = CollectionUtils.concat(getPath(), getCode());
        attribute.setPath(pathToAddedAttribute);
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
    public BattleAttribute clone() {
        AttributesBasedAttribute clonedAttribute = new AttributesBasedAttribute(getCode());

        for (BattleAttribute attribute : attributes.values()) {
            clonedAttribute.add(attribute.clone());
        }

        return clonedAttribute.setPath(getPath());
    }

    @Override
    public String toString() {
        return "AttributesBasedAttribute [" + getCode() + "]";
    }
}
