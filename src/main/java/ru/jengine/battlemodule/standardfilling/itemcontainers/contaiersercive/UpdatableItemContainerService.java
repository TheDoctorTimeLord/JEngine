package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import javax.annotation.Nullable;

import ru.jengine.battlemodule.core.modelattributes.AttributeStore;
import ru.jengine.battlemodule.core.state.BattleState;

/**
 * Расширение сервиса {@link ItemContainerService}, позволяющее изменять данные в сервисе
 */
public interface UpdatableItemContainerService extends ItemContainerService {
    /**
     * Инициализирует состояние контейнера данными о начальном состоянии боля
     * @param preparedBattleState состояние боя, на момент старта
     */
    void initialize(BattleState preparedBattleState);

    /**
     * Экипирует предмет на объект на поле боя. Предмет описывается набором данных о том, как его можно найти в
     * некотором контейнере в бою. Дополнительно передаётся хранилище, в которое нужно поместить предмет, и код
     * атрибута, в котором он будет хранится
     *
     * @param modelId ID объекта, которому экипируется предмет
     * @param itemPlaceName имя атрибута, в который добавляется информация о предмете
     * @param equippedPlace место, куда будет добавлен атрибут с именем containerPlaceName и данными о предмете
     * @param containerCode код контейнера, из которого берётся предмет
     * @param itemIndex индекс предмета в контейнере
     */
    void equipItem(int modelId, String itemPlaceName, AttributeStore<?> equippedPlace, String containerCode,
            int itemIndex);

    /**
     * Убирает предмет из экипировки объекта, сохраняя атрибут, в котором он хранился
     *
     * @param modelId ID объекта, у которого удаляется предмет из экипировки
     * @param containerCode код контейнера, в котором на данный момент находится элемент
     * @param itemIndex индекс элемента в контейнере
     * @return атрибут, в котором находился убранный предмет
     */
    @Nullable
    ItemAggregator takeOffItem(int modelId, String containerCode, int itemIndex);

    /**
     * Добавляет объекту переносимый им контейнер. Дополнительно передаётся хранилище, в которое нужно поместить
     * контейнер, и код атрибута, в котором он будет хранится
     *
     * @param modelId ID объекта, которому добавляется переносимый контейнер
     * @param containerPlaceName имя атрибута, в который добавляется информация о контейнере
     * @param containerCode код переносимого контейнера
     * @param equippedPlace место, куда будет добавлен атрибут с именем containerPlaceName и данными о контейнере
     */
    void equipItemContainer(int modelId, String containerPlaceName, String containerCode, AttributeStore<?> equippedPlace);

    /**
     * Убирает переносимый персонажем контейнер, сохраняя атрибут, в котором он хранился
     *
     * @param modelId ID объекта, у которого убирается переносимый контейнер
     * @param containerCode код убираемого контейнера
     * @return атрибут, в котором находился убранный контейнер
     */
    @Nullable
    ItemContainerAggregator takeOffItemContainer(int modelId, String containerCode);
}
