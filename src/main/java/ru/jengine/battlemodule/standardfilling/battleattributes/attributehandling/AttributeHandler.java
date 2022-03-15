package ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling;

import java.util.List;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.handlingconditions.HandlingCondition;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.processedattributes.AbstractProcessedAttribute;

/**
 * Обработчик изменённых атрибутов некоторой модели на поле боя. Может изменять другие атрибуты в результате
 * изменения состояния атрибутов, подходящих под условия, которые были возвращены методом
 * {@link #getHandledAttributeCodes()}
 */
public interface AttributeHandler {
    /**
     * Возвращает набор условий, которым должен удовлетворять изменённый атрибут, чтобы он мог быть обработан данным
     * обработчиком
     */
    List<HandlingCondition> getHandledAttributeCodes();

    /**
     * Обработка добавленного или изменённого атрибута модели в бою
     * @param model модель объекта в бою, которому принадлежит изменённый атрибут
     * @param puttedAttribute атрибут, который был изменён и подошёл под условия из {@link #getHandledAttributeCodes()}
     * @return набор атрибутов, которые были изменены в результате обработки
     */
    List<AbstractProcessedAttribute> processPuttedAttribute(BattleModel model, BattleAttribute puttedAttribute);

    /**
     * Обработка удалённого атрибута модели в бою
     * @param model модель объекта в бою, которому принадлежал удалённый атрибут
     * @param removedAttribute атрибут, который был удалён и подошёл под условия из {@link #getHandledAttributeCodes()}
     * @return набор атрибутов, которые были изменены в результате обработки
     */
    List<AbstractProcessedAttribute> processRemovedAttribute(BattleModel model, BattleAttribute removedAttribute);
}
