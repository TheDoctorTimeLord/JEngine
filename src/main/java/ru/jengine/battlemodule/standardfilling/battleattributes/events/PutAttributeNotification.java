package ru.jengine.battlemodule.standardfilling.battleattributes.events;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Событие, уведомляющее (В ПЕРВУЮ ОЧЕРЕДЬ СИСТЕМУ ОБРАБОТКИ ИЗМЕНЕНИЯ АТРИБУТОВ) об добавлении, либо изменении
 * значений некоторого атрибута. ВАЖНО: система обработки атрибутов не уведомляет об изменении атрибутов, которое
 * произошло внутри этой системы
 */
public class PutAttributeNotification extends ChangedAttributeNotification {
    private final int modelId;
    private final BattleAttribute puttedAttribute;

    public PutAttributeNotification(int modelId, BattleAttribute puttedAttribute) {
        this.modelId = modelId;
        this.puttedAttribute = puttedAttribute;
    }

    public BattleAttribute getPuttedAttribute() {
        return puttedAttribute;
    }

    public int getModelId() {
        return modelId;
    }
}
