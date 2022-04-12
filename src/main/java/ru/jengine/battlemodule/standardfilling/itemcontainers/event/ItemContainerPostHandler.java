package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.standardfilling.BattleEventHandlerPriority;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.UpdatableItemContainerService;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Абстрактный класс для всех {@link PostHandler обработчиков} событий изменения данных об используемых персонажами
 * предметах или переносимых контейнерах
 * @param <E> тип обрабатываемого события
 */
public abstract class ItemContainerPostHandler<E extends BattleEvent> implements PostHandler<E> {
    /**
     * Сервис, через который происходит получение данных о контейнерах и элементах в них
     */
    protected final UpdatableItemContainerService itemContainerService;

    public ItemContainerPostHandler(UpdatableItemContainerService itemContainerService) {
        this.itemContainerService = itemContainerService;
    }

    @Override
    public int getPriority() {
        return BattleEventHandlerPriority.HANDLE.getPriority();
    }
}
