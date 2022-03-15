package ru.jengine.battlemodule.core.modelattributes.baseattributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.AttributeRuntimeException;

/**
 * Атрибут, хранящий в качестве дополнительной информации другие атрибуты
 */
public class AttributesBasedAttribute extends BattleAttribute {
    private final Map<String, BattleAttribute> attributes = new HashMap<>();

    public AttributesBasedAttribute(String code) {
        super(code);
    }

    /**
     * Получение атрибута по его коду
     * @param attributeCode код атрибута
     * @param <T> ожидаемый тип атрибута
     * @return атрибут или null, если атрибута с таким кодом не было найдено
     */
    public <T extends BattleAttribute> T get(String attributeCode) {
        return (T)attributes.get(attributeCode);
    }

    /**
     * Получение строкового атрибута по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является строковым
     * @return строковый атрибут или null, если атрибута с таким кодом не было найдено
     */
    public StringAttribute getAsString(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not string".formatted(attribute), e);
        }
    }

    /**
     * Получение целочисленного атрибута по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является целочисленным
     * @return целочисленный атрибут или null, если атрибута с таким кодом не было найдено
     */
    public IntAttribute getAsInt(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not int".formatted(attribute), e);
        }
    }

    /**
     * Получение атрибута с плавающей точкой по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является числом с плавающей точкой
     * @return атрибут с плавающей точкой атрибут или null, если атрибута с таким кодом не было найдено
     */
    public FloatAttribute getAsFloat(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not float".formatted(attribute), e);
        }
    }

    /**
     * Получение логического атрибута по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является логическим
     * @return логический атрибут или null, если атрибута с таким кодом не было найдено
     */
    public BooleanAttribute getAsBoolean(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not boolean".formatted(attribute), e);
        }
    }

    /**
     * Получение атрибута, хранящего атрибуты, по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является хранящим атрибуты
     * @return атрибут, хранящий атрибуты, или null, если атрибута с таким кодом не было найдено
     */
    public AttributesBasedAttribute getAsContainer(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not attribute based".formatted(attribute), e);
        }
    }

    /**
     * Добавление нового атрибута
     * @param attribute добавляемый атрибут
     * @return текущий объект
     */
    public AttributesBasedAttribute add(BattleAttribute attribute) {
        List<String> pathToAddedAttribute = new ArrayList<>(getPath());
        pathToAddedAttribute.add(getCode());
        attribute.setPath(pathToAddedAttribute);
        attributes.put(attribute.getCode(), attribute);

        return this;
    }

    /**
     * Удаление атрибута
     * @param attributeCode код атрибута
     */
    public void remove(String attributeCode) {
        attributes.remove(attributeCode);
    }

    /**
     * Удаление всех атрибутов
     */
    public void removeAll() {
        attributes.clear();
    }

    @Override
    public String toString() {
        return "AttributesBasedAttribute [" + getCode() + "]";
    }
}
