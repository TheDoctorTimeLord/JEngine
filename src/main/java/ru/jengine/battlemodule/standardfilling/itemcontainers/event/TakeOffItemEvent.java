package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.ItemData;

/**
 * Событие убирающее предмет из экипировки объекта. Оставляет атрибут, из которого удаляется предмет
 */
public class TakeOffItemEvent extends BattleEvent {
    private final int modelId;
    private final String containerCode;
    private final int itemIndex;

    /**
     * @param modelId ID объекта, у которого удаляется предмет из экипировки
     * @param containerCode код контейнера, в котором на данный момент находится элемент
     * @param itemIndex индекс элемента в контейнере
     */
    public TakeOffItemEvent(int modelId, String containerCode, int itemIndex) {
        this.modelId = modelId;
        this.containerCode = containerCode;
        this.itemIndex = itemIndex;
    }

    /**
     * @param modelId ID объекта, у которого удаляется предмет из экипировки
     * @param itemData данные об удаляемого предмете
     */
    public TakeOffItemEvent(int modelId, ItemData itemData) {
        this(modelId, itemData.getContainerCode(), itemData.getItemPosition());
    }

    public String getContainerCode() {
        return containerCode;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public int getModelId() {
        return modelId;
    }
}
