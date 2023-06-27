package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.UpdatableItemContainerService;

/**
 * Обработчик события {@link TakeOffItemEvent}
 */
public class TakeOffItemEventHandler extends ItemContainerPostHandler<TakeOffItemEvent> {
    public TakeOffItemEventHandler(UpdatableItemContainerService itemContainerService) {
        super(itemContainerService);
    }

    @Override
    public void handle(TakeOffItemEvent event) {
        itemContainerService.takeOffItem(event.getModelId(), event.getContainerCode(), event.getItemIndex());
    }
}
