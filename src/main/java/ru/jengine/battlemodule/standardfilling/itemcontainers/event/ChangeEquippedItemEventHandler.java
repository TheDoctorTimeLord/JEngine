package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.ItemAggregator;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.UpdatableItemContainerService;

/**
 * Обработчик для события {@link ChangeEquippedItemEvent}
 */
public class ChangeEquippedItemEventHandler extends ItemContainerPostHandler<ChangeEquippedItemEvent> {
    public ChangeEquippedItemEventHandler(UpdatableItemContainerService itemContainerService) {
        super(itemContainerService);
    }

    @Override
    public void handle(ChangeEquippedItemEvent event) {
        ItemAggregator itemAggregator = itemContainerService.takeOffItem(
                event.getModelId(),
                event.getFromContainer(),
                event.getIndexFromContainer()
        );

        if (itemAggregator == null) {
            return;
        }

        itemContainerService.equipItem(
                event.getModelId(),
                itemAggregator.getCode(),
                event.getAttributeContainer().getAttributeByPath(itemAggregator.getPath()),
                event.getToContainer(),
                event.getIndexToContainer()
        );
    }
}
