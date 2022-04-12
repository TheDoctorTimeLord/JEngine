package ru.jengine.battlemodule.standardfilling.itemcontainers;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.contentregistrars.AbstractContentRegistrar;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.ItemContainerService;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.ItemContainerServiceImpl;
import ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive.UpdatableItemContainerService;
import ru.jengine.battlemodule.standardfilling.itemcontainers.event.EquipItemEventHandler;
import ru.jengine.battlemodule.standardfilling.itemcontainers.event.ChangeEquippedItemEventHandler;
import ru.jengine.battlemodule.standardfilling.itemcontainers.event.TakeOffItemEventHandler;

/**
 * Регистратор всего контента, который связан с работой с предметами конкретного персонажа
 */
@BattleBeanPrototype
public class ItemContainerServiceRegistrar extends AbstractContentRegistrar {
    @Override
    protected void registerInt() {
        BattleState state = battleContext.getBattleState();
        DispatcherBattleWrapper dispatcher = battleContext.getDispatcher();

        UpdatableItemContainerService itemContainerService = new ItemContainerServiceImpl(state, dispatcher);

        registerInformationService(ItemContainerService.class, itemContainerService);

        registerPostHandler(new EquipItemEventHandler(itemContainerService));
        registerPostHandler(new TakeOffItemEventHandler(itemContainerService));
        registerPostHandler(new ChangeEquippedItemEventHandler(itemContainerService));

        registerTaskAfterInitializeBattle(() -> itemContainerService.initialize(state));
    }
}
