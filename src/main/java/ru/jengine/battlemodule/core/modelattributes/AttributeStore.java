package ru.jengine.battlemodule.core.modelattributes;

import java.util.Collection;

import ru.jengine.battlemodule.core.modelattributes.baseattributes.AttributesBasedAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.BooleanAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.FloatAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.IntAttribute;
import ru.jengine.battlemodule.core.modelattributes.baseattributes.StringAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.AttributeRuntimeException;

/**
 * Интерфейс, описывающий объект, который может хранить другие атрибуты
 */
public interface AttributeStore<S> {
    /**
     * Получение атрибута по его коду
     * @param attributeCode код атрибута
     * @param <T> ожидаемый тип атрибута
     * @return атрибут или null, если атрибута с таким кодом не было найдено
     */
    <T extends BattleAttribute> T get(String attributeCode);

    /**
     * Добавление нового атрибута
     * @param attribute добавляемый атрибут
     * @return текущий объект
     */
    S add(BattleAttribute attribute);

    /**
     * Удаление атрибута
     * @param attributeCode код атрибута
     * @return атрибут, который был удалён, либо null, если ни один атрибут не был удалён
     */
    BattleAttribute remove(String attributeCode);

    /**
     * Удаление всех атрибутов
     */
    void removeAll();

    /**
     * Возвращает все атрибуты, которые находятся в хранилище
     */
    Collection<BattleAttribute> getAttributes();

    /**
     * Получение строкового атрибута по его коду
     * @param attributeCode код атрибута
     * @throws AttributeRuntimeException если атрибут не является строковым
     * @return строковый атрибут или null, если атрибута с таким кодом не было найдено
     */
    default StringAttribute getAsString(String attributeCode) {
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
    default IntAttribute getAsInt(String attributeCode) {
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
    default FloatAttribute getAsFloat(String attributeCode) {
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
    default BooleanAttribute getAsBoolean(String attributeCode) {
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
    default AttributesBasedAttribute getAsContainer(String attributeCode) {
        try {
            return get(attributeCode);
        } catch (ClassCastException e) {
            BattleAttribute attribute = get(attributeCode);
            throw new AttributeRuntimeException("Attribute [%s] is not attribute based".formatted(attribute), e);
        }
    }

    /**
     * Проверяет, есть ли атрибут с таким кодов в хранилище атрибутов
     * @param attributeCode код атрибута
     * @return true - если такой атрибут есть в хранилище, false - в противном случае
     */
    default boolean contains(String attributeCode) {
        return get(attributeCode) != null;
    }
}
