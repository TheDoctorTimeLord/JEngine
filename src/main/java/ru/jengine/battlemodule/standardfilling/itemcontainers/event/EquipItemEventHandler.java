package ru.jengine.battlemodule.standardfilling.itemcontainers.event;

import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.UpdatableItemContainerService;

/**
 * Обработчик события {@link EquipItemEvent}
 */
public class EquipItemEventHandler extends ItemContainerPostHandler<EquipItemEvent> {
    public EquipItemEventHandler(UpdatableItemContainerService itemContainerService) {
        super(itemContainerService);
    }

    @Override
    public void handle(EquipItemEvent event) {
        itemContainerService.equipItem(event.getModelId(), event.getContainerPlaceName(), event.getEquippedPlace(),
                event.getContainerCode(), event.getItemIndex());
    }
}
