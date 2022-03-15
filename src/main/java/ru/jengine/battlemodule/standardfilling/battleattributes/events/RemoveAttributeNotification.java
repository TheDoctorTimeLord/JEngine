package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Событие, уведомляющее (В ПЕРВУЮ ОЧЕРЕДЬ СИСТЕМУ ОБРАБОТКИ ИЗМЕНЕНИЯ АТРИБУТОВ) об удалении некоторого атрибута.
 * ВАЖНО: система обработки атрибутов не уведомляет об удалении атрибутов, которое произошла внутри этой системы
 */
public class RemoveAttributeNotification extends ChangedAttributeNotification {
    private final int modelId;
    private final BattleAttribute removedAttribute;

    public RemoveAttributeNotification(int modelId, BattleAttribute removedAttribute) {
        this.modelId = modelId;
        this.removedAttribute = removedAttribute;
    }

    public int getModelId() {
        return modelId;
    }

    public BattleAttribute getRemovedAttribute() {
        return removedAttribute;
    }
}
