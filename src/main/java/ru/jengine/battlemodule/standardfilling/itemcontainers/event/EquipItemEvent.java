package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.modelattributes.AttributeStore;

/**
 * Событие экипировки предмета. Добавляет атрибут, в который помещается предмет, если его до этого её не существовало
 */
public class EquipItemEvent extends BattleEvent {
    private final int modelId;
    private final String containerPlaceName;
    private final String containerCode;
    private final int itemIndex;
    private final AttributeStore<?> equippedPlace;

    /**
     * @param modelId ID объекта, которому экипируется предмет
     * @param containerPlaceName имя атрибута, в который добавляется информация о предмете
     * @param containerCode код контейнера, из которого берётся предмет
     * @param itemIndex индекс предмета в контейнере
     * @param equippedPlace место, куда будет добавлен атрибут с именем containerPlaceName и данными о предмете
     */
    public EquipItemEvent(int modelId, String containerPlaceName, String containerCode, int itemIndex,
            AttributeStore<?> equippedPlace) {
        this.modelId = modelId;
        this.containerPlaceName = containerPlaceName;
        this.containerCode = containerCode;
        this.itemIndex = itemIndex;
        this.equippedPlace = equippedPlace;
    }

    public String getContainerPlaceName() {
        return containerPlaceName;
    }

    public int getModelId() {
        return modelId;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public AttributeStore<?> getEquippedPlace() {
        return equippedPlace;
    }
}
