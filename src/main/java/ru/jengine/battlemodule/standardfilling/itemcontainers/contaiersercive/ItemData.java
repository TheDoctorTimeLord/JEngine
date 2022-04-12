package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import com.google.common.base.Objects;

/**
 * Метаданные, соответствующие конкретному предмету в некотором контейнере
 */
public class ItemData {
    /** Код контейнера, в котором находится предмет */
    private final String containerCode;
    /** Индекс предмета в контейнере */
    private final int itemPosition;
    /** Глобальный тип предмета (не относится к {@link ru.jengine.battlemodule.core.containers.Item}) */
    private final String itemType;

    public ItemData(String containerCode, int itemPosition, String itemType) {
        this.itemType = itemType;
        this.containerCode = containerCode;
        this.itemPosition = itemPosition;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public String getItemType() {
        return itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ItemData itemData))
            return false;
        return itemPosition == itemData.itemPosition && Objects.equal(containerCode,
                itemData.containerCode) && Objects.equal(itemType, itemData.itemType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(containerCode, itemPosition, itemType);
    }
}
