package ru.jengine.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.modelattributes.AttributeStore;
import ru.jengine.battlemodule.core.modelattributes.AttributesContainer;
import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.PutAttributeNotification;
import ru.jengine.battlemodule.standardfilling.battleattributes.events.RemoveAttributeNotification;

/**
 * Утилитарные методы для обработки атрибутов
 */
public class AttributeUtils {
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

    /**
     * Получение последнего атрибута, в переданном пути. Является аналогией для метода
     * {@link #extractInnerAttribute(AttributesContainer, List, String)}, доставая последний аргумент из последнего
     * элемента в path
     *
     * @param container контейнер, в рамках которого происходит получение атрибута
     * @param path путь по атрибутам от container до получаемого атрибута (по вложенным друг в друга атрибутам)
     * @param <T> ожидаемый тип получаемого атрибута
     * @return последний атрибут в цепочке path, либо null, если атрибута по такому пути нет
     */
    @Nullable
    public static <T extends BattleAttribute> T extractLastAttributeInPath(AttributesContainer container,
            List<String> path)
    {
        List<String> coppedPath = new ArrayList<>(path);
        String attributeName = CollectionUtils.popLast(coppedPath);
        return attributeName == null ? null : extractInnerAttribute(container, coppedPath, attributeName);
    }

    /**
     * Получение атрибута, в переданном пути, с заданным кодом
     *
     * @param container контейнер, в рамках которого происходит получение атрибута
     * @param path путь по атрибутам от container до атрибута, хранящего искомый (по вложенным друг в друга атрибутам)
     * @param attributeName код искомого атрибута
     * @param <T> ожидаемый тип получаемого атрибута
     * @return искомый атрибут, либо null, если атрибута по такому пути нет
     */
    @Nullable
    public static <T extends BattleAttribute> T extractInnerAttribute(AttributesContainer container, List<String> path,
            String attributeName)
    {
        BattleAttribute currentAttributeStore = new AttributeContainerWrapper(container);
        Queue<String> attributesQueue = new ArrayDeque<>(path);

        while (!attributesQueue.isEmpty() && currentAttributeStore instanceof AttributeStore<?> store) {
            String innerAttributeCode = attributesQueue.poll();
            currentAttributeStore = store.get(innerAttributeCode);

            if (currentAttributeStore == null) {
                return null;
            }
        }

        return currentAttributeStore instanceof AttributeStore<?>
                ? ((AttributeStore<?>)currentAttributeStore).get(attributeName)
                : null;
    }

    private static class AttributeContainerWrapper extends BattleAttribute implements AttributeStore<AttributesContainer> {
        private final AttributesContainer wrapped;

        protected AttributeContainerWrapper(AttributesContainer wrapped) {
            super("");
            this.wrapped = wrapped;
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
