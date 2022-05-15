package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Описывает объект, отвечающий за подбор {@link AttributeRule обработчиков атрибутов}, которые могут обработать
 * изменённый атрибут
 */
public interface AttributeRulesFinder {
    /**
     * подбор {@link AttributeRule обработчиков атрибутов}, которые могут обработать изменённый атрибут
     * @param changedAttribute атрибут модели, который был изменён
     * @return Подходящие обработчики атрибутов. Если по подходящих обработчиков нет, то вернётся пустой список
     */
    List<AttributeRule> findAttributeHandlers(BattleAttribute changedAttribute);
}
