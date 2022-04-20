package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.modelattributes.BattleAttribute;

/**
 * Атрибут, хранящий данные об экипированном предмете
 */
public class ItemAggregator extends BattleAttribute {
    private ItemData itemData;

    public ItemAggregator(String code, @Nullable ItemData itemData) {
        super(code);
        this.itemData = itemData;
    }

    /**
     * Получение метаданных экипированного предмета
     */
    @Nullable
    public ItemData getItemData() {
        return itemData;
    }

    /**
     * Устанавливает метаданные экипируемого предмета
     * @param itemData метаданные экипируемого предмета
     */
    public void setItemData(@Nullable ItemData itemData) {
        this.itemData = itemData;
    }

    @Override
    public BattleAttribute clone() {
        return new ItemAggregator(getCode(), getItemData()).setPath(getPath());
    }
}
