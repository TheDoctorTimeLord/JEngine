package ru.jengine.battlemodule.standardfilling.battleattributes.attributerules;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes.AbstractProcessedAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes.PuttedProcessedAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.processedattributes.RemovedProcessedAttribute;

/**
 * Главный менеджер обработки атрибутов. Он организует работу системы изменения атрибутов, которые были зависимы от
 * изменившихся в процессе работы атрибутов. Другими словами менеджер организует процесс реакции на изменение
 * атрибутов. Менеджер поддерживает реакцию на разные типы изменений, например, на удаление или добавление.
 */
public class AttributeRulesManager {
    private final AttributeRulesFinder attributeRulesFinder;
    private final DispatcherBattleWrapper dispatcher;

    public AttributeRulesManager(AttributeRulesFinder attributeRulesFinder, DispatcherBattleWrapper dispatcher) {
        this.attributeRulesFinder = attributeRulesFinder;
        this.dispatcher = dispatcher;
    }

    /**
     * Обработка добавленного или изменённого атрибута модели в бою
     * @param model модель объекта в бою, которому принадлежит изменённый атрибут
     * @param puttedAttribute атрибут модели, который был изменён
     */
    public void processPuttedAttribute(BattleModel model, BattleAttribute puttedAttribute) {
        processAttribute(model, new PuttedProcessedAttribute(puttedAttribute));
    }

    /**
     * Обработка удалённого атрибута модели в бою
     * @param model модель объекта в бою, которому принадлежит изменённый атрибут
     * @param removedAttribute атрибут модели, который был удалён
     */
    public void processRemovedAttribute(BattleModel model, BattleAttribute removedAttribute) {
        processAttribute(model, new RemovedProcessedAttribute(removedAttribute));
    }

    private void processAttribute(BattleModel model, AbstractProcessedAttribute processedAttribute)
    {
        Queue<AbstractProcessedAttribute> attributesQueue = new ArrayDeque<>();
        attributesQueue.add(processedAttribute);

        while (!attributesQueue.isEmpty()) {
            AbstractProcessedAttribute changedAttribute = attributesQueue.poll();

            List<AttributeRule> availableHandlers =
                    attributeRulesFinder.findAttributeHandlers(changedAttribute.getAttribute());

            for (AttributeRule handler : availableHandlers) {
                attributesQueue.addAll(changedAttribute.processAttribute(handler, model, dispatcher));
            }
        }
    }
}
