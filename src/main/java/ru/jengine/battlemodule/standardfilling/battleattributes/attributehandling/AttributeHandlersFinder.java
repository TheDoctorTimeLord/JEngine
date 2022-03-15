package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Описывает объект, отвечающий за подбор {@link AttributeHandler обработчиков атрибутов}, которые могут обработать
 * изменённый атрибут
 */
public interface AttributeHandlersFinder {
    /**
     * подбор {@link AttributeHandler обработчиков атрибутов}, которые могут обработать изменённый атрибут
     * @param changedAttribute атрибут модели, который был изменён
     * @return Подходящие обработчики атрибутов. Если по подходящих обработчиков нет, то вернётся пустой список
     */
    List<AttributeHandler> findAttributeHandlers(BattleAttribute changedAttribute);
}
