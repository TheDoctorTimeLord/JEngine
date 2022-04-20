package ru.jengine.battlemodule.core.containers;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.BattleBeanPrototype;

/**
 * Объект, хранящий данные обо всех доступных контейнерах предметов в бою
 */
@BattleBeanPrototype
public class ItemContainersManager {
    private final Map<String, ItemContainer> containers = new ConcurrentHashMap<>();

    /**
     * Добавление контейнера предметов
     *
     * @param itemContainer добавляемый контейнер
     * @return контейнер, который был добавлен
     */
    public ItemContainer createContainer(ItemContainer itemContainer) {
        return containers.compute(itemContainer.getContainerCode(), (code, oldValue) -> {
            if (oldValue == null) {
                return itemContainer;
            }
            throw new ItemRuntimeException(("Container with code [%s] was exist. Adding container [%s]. Existing "
                    + "container [%s]").formatted(code, itemContainer, oldValue));
        });
    }

    /**
     * Удаляет контейнер с переданным кодом
     *
     * @param containerName код удаляемого контейнера
     */
    public void removeContainer(String containerName) {
        containers.remove(containerName);
    }

    /**
     * Получение контейнера по коду
     *
     * @param containerCode код получаемого контейнера
     * @return контейнер предметов или null, если контейнера с таким кодом нет
     */
    @Nullable
    public ItemContainer getContainer(String containerCode) {
        return containers.get(containerCode);
    }

    public Collection<Item> getContainedItems() {
        return containers.values().stream()
                .map(ItemContainer::getContainedItems)
                .flatMap(Collection::stream)
                .map(ItemStack::getItem)
                .collect(Collectors.toList());
    }
}
