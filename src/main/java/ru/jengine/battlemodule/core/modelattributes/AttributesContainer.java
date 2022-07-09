package ru.jengine.battlemodule.core.modelattributes;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import ru.jengine.utils.AttributeUtils.AttributeContainerWrapper;
import ru.jengine.utils.CollectionUtils;

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

    /**
     * Получение последнего атрибута, в переданном пути элемента в path
     *
     * @param path путь по вложенным друг в друга атрибутам от container до получаемого атрибута
     * @param <T> ожидаемый тип получаемого атрибута
     * @return последний атрибут в цепочке path, либо null, если атрибута по такому пути нет или был передан пустой
     *         список
     */
    @Nullable
    public <T extends BattleAttribute> T getAttributeByPath(String... path) {
        return getAttributeByPath(CollectionUtils.concat((Object[])path));
    }

    /**
     * Получение последнего атрибута, в переданном пути элемента в path
     *
     * @param path путь по вложенным друг в друга атрибутам от container до получаемого атрибута
     * @param <T> ожидаемый тип получаемого атрибута
     * @return последний атрибут в цепочке path, либо null, если атрибута по такому пути нет или был передан пустой
     *         список
     */
    @Nullable
    public <T extends BattleAttribute> T getAttributeByPath(List<String> path) {
        BattleAttribute currentAttributeStore = new AttributeContainerWrapper(this);
        String resultAttributeName = CollectionUtils.popLast(path);

        Queue<String> attributesQueue = new ArrayDeque<>(path);

        while (!attributesQueue.isEmpty() && currentAttributeStore instanceof AttributeStore<?> store) {
            String innerAttributeCode = attributesQueue.poll();
            currentAttributeStore = store.get(innerAttributeCode);

            if (currentAttributeStore == null) {
                return null;
            }
        }

        return currentAttributeStore instanceof AttributeStore<?>
                ? ((AttributeStore<?>)currentAttributeStore).get(resultAttributeName)
                : null;
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
