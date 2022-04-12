package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import java.util.Collection;

import ru.jengine.battlemodule.core.information.InformationService;

/**
 * Сервис, предоставляющий данные о переносимых персонажами {@link ItemContainerAggregator контейнерах} и используемых
 * {@link ItemAggregator предметах}. Предоставляет собранные данные, построенные на основании специальных атрибутов
 * персонажей
 */
public interface ItemContainerService extends InformationService {
    /**
     * Получение всех экипированных персонажем предметов
     *
     * @param modelId ID персонажа, предметы которого запрашиваются
     * @return Коллекция атрибутов предметов. Если никаких предметов не экипировано, то возвращается пустая коллекция
     */
    Collection<ItemAggregator> getEquippedItems(int modelId);

    /**
     * Получение всех переносимых персонажем контейнеров
     *
     * @param modelId ID персонажа, предметы которого запрашиваются
     * @return Коллекция атрибутов контейнеров. Если никаких контейнеров не экипировано, то возвращается пустая
     * коллекция
     */
    Collection<ItemContainerAggregator> getEquippedContainers(int modelId);
}
