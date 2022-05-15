package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import java.util.List;

import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.handlingconditions.HandlingCondition;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes.AbstractProcessedAttribute;

/**
 * Правило, по которому изменяются атрибуты некоторой модели на поле боя. Изменение других атрибутов происходит в
 * результате изменения состояния атрибутов, подходящих под условия, которые были возвращены методом
 * {@link #getHandledAttributeCodes()}
 */
public interface AttributeRule {
    /**
     * Возвращает набор условий, которым должен удовлетворять изменённый атрибут, чтобы он мог быть обработан данным
     * обработчиком
     */
    List<HandlingCondition> getHandledAttributeCodes();

    /**
     * Обработка добавленного или изменённого атрибута модели в бою.
     *
     *
     * @return набор атрибутов, которые были изменены в результате обработки
     */
    List<AbstractProcessedAttribute> processPuttedAttribute(ChangedAttributesContext context);

    /**
     * Обработка удалённого атрибута модели в бою
     *
     * @param context контекст обрабатываемого атрибута
     * @return набор атрибутов, которые были изменены в результате обработки
     */
    List<AbstractProcessedAttribute> processRemovedAttribute(ChangedAttributesContext context);
}
