package ru.jengine.utils;

import java.util.Collection;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.AttributeStore;
import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.attributerules.AttributeRulesManager;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotification;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.RemoveAttributeNotification;

/**
 * Утилитарные методы для обработки атрибутов
 */
public class AttributeUtils {
    /**
     * Уведомляет
     * {@link AttributeRulesManager
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
     * {@link AttributeRulesManager
     * систему обработки атрибутов} об удалении некоторого атрибута
     * @param modelId ID объекта, которому принадлежал этот атрибут
     * @param dispatcher диспетчер событий внутри боя
     * @param attribute удалённый атрибут
     */
    public static void notifyAboutRemove(int modelId, DispatcherBattleWrapper dispatcher, BattleAttribute attribute) {
        dispatcher.handle(new RemoveAttributeNotification(modelId, attribute));
    }

    public static class AttributeContainerWrapper extends BattleAttribute implements AttributeStore<AttributesContainer> {
        private final AttributesContainer wrapped;

        public AttributeContainerWrapper(AttributesContainer wrapped) {
            super("");
            this.wrapped = wrapped;
        }

        @Override
        public BattleAttribute clone() {
            return new AttributeContainerWrapper(wrapped.clone());
        }

        @Override
        public <T extends BattleAttribute> T get(String attributeCode) {
            return wrapped.get(attributeCode);
        }

        @Override
        public AttributesContainer add(BattleAttribute attribute) {
            return wrapped.add(attribute);
        }

        @Override
        public BattleAttribute remove(String attributeCode) {
            return wrapped.remove(attributeCode);
        }

        @Override
        public void removeAll() {
            wrapped.removeAll();
        }

        @Override
        public Collection<BattleAttribute> getAttributes() {
            return wrapped.getAttributes();
        }
    }
}
