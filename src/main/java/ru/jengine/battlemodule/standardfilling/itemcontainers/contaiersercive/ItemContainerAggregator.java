package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий данные о носимом персонажем контейнере
 */
public class ItemContainerAggregator extends BattleAttribute { //TODO объединить с ItemAggregator?
    private ItemContainerData itemContainerData;

    public ItemContainerAggregator(String code, @Nullable ItemContainerData itemContainerData) {
        super(code);
        this.itemContainerData = itemContainerData;
    }

    /**
     * Получение метаданных носимого контейнера
     */
    @Nullable
    public ItemContainerData getItemContainerData() {
        return itemContainerData;
    }

    /**
     * Устанавливает метаданные носимого контейнера
     * @param itemContainerData метаданные носимого контейнера
     */
    public void setItemContainerData(@Nullable ItemContainerData itemContainerData) {
        this.itemContainerData = itemContainerData;
    }

    @Override
    public BattleAttribute clone() {
        return new ItemContainerAggregator(getCode(), getItemContainerData()).setPath(getPath());
    }
}
