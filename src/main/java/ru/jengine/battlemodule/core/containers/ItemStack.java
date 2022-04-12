package ru.jengine.battlemodule.core.containers;

import java.util.Objects;

/**
 * Набор предметов некоторого {@link Item типа}
 */
public class ItemStack {
    private final Item item;
    private int itemCount;

    public ItemStack(Item item, int itemCount) {
        Objects.requireNonNull(item, "Item must be not null");
        this.item = item;
        this.itemCount = itemCount;

        checkItemCount(itemCount);
    }

    /**
     * Получение типа предметов к которому относится данный набор
     */
    public Item getItem() {
        return item;
    }

    /**
     * Изменение количества предметов
     *
     * @param changedValue значение, на которое изменяется количество предметов
     * @throws ItemRuntimeException если количество становится отрицательным или больше максимально возможного для
     * данного {@link Item типа} предметов
     */
    public void changeCount(int changedValue) {
        itemCount += changedValue;
        checkItemCount(itemCount);
    }

    /**
     * Получение текущего количества предметов в наборе
     */
    public int getItemCount() {
        return itemCount;
    }

    private void checkItemCount(int itemCount) {
        if (itemCount < 0 || itemCount > item.getMaxItemCount()) {
            throw new ItemRuntimeException("Item count of [%s] must be in [0, %s], but actual [%s]"
                    .formatted(item.getMaxItemCount(), item, itemCount));
        }
    }
}
