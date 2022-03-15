package ru.jengine.utils;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotification;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.RemoveAttributeNotification;

/**
 * Утилитарные методы для обработки атрибутов
 */
public class AttributeHandlingUtils {
    /**
     * Уведомляет
     * {@link ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandlerManager
     * систему обработки атрибутов} об изменении некоторого атрибута
     * @param modelId ID объекта, которому принадлежит этот атрибут
     * @param dispatcher диспетчер событий внутри боя
     * @param attribute изменённый атрибут
     */
    public static void notifyAboutChange(int modelId, DispatcherBattleWrapper dispatcher, BattleAttribute attribute) {
        dispatcher.handle(new PutAttributeNotification(modelId, attribute));
    }

    /**
     * Уведомляет
     * {@link ru.jengine.battlemodule.standardfilling.battleattributes.attributehandling.AttributeHandlerManager
     * систему обработки атрибутов} об удалении некоторого атрибута
     * @param modelId ID объекта, которому принадлежал этот атрибут
     * @param dispatcher диспетчер событий внутри боя
     * @param attribute удалённый атрибут
     */
    public static void notifyAboutRemove(int modelId, DispatcherBattleWrapper dispatcher, BattleAttribute attribute) {
        dispatcher.handle(new RemoveAttributeNotification(modelId, attribute));
    }
}
