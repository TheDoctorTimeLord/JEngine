package ru.jengine.battlemodule.standartfilling.movement;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.battlemodule.core.events.EventHandlerBinder;
import ru.jengine.battlemodule.core.models.BattleModel;
import ru.jengine.battlemodule.core.models.HasPosition;
import ru.jengine.battlemodule.core.state.BattleState;
import ru.jengine.eventqueue.event.PostHandler;

@BattleBeanPrototype
public class MovementActionRegistrar implements EventHandlerBinder {
    private static class MovementHandler implements PostHandler<MoveEvent> {
        private final BattleState battleState;

        private MovementHandler(BattleState battleState) {
            this.battleState = battleState;
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        public Class<MoveEvent> getHandlingEventType() {
            return MoveEvent.class;
        }

        @Override
        public void handle(MoveEvent event) {
            int id = event.getModelId();

            BattleModel model = battleState.resolveId(id);
            HasPosition moved = HasPosition.castToHasPosition(model);
            if (moved == null) {
                return; //TODO залогировать
            }
            moved.setPosition(event.getNewPosition());

            battleState.removeFromPosition(event.getOldPosition(), id);
            battleState.setToPosition(event.getNewPosition(), id);
        }
    }

    private MovementHandler handler;

    @Override
    public void bindPostHandlerToEvent(BattleContext battleContext) {
        handler = new MovementHandler(battleContext.getBattleState());
        battleContext.getDispatcher().registerPostHandler(handler);
    }

    @Override
    public void unbindPostHandlerToEvent(DispatcherBattleWrapper dispatcher) {
        dispatcher.removePostHandler(handler);
    }
}
