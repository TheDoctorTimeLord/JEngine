package ru.jengine.battlemodule.core.events;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.eventqueue.Dispatcher;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;

@BattleBeanPrototype
public class DispatcherBattleWrapper {
    private final Dispatcher dispatcher;
    private String battleId;

    public DispatcherBattleWrapper(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void setBattleId(String battleCode) {
        this.battleId = battleCode;
    }

    public void registerFastHandler(List<EventInterceptor> interceptors, EventPoolHandler handler) {
        dispatcher.registerFastHandler(battleId, interceptors, handler);
    }

    public void removeFastHandler() {
        dispatcher.removeFastHandler(battleId);
    }

    public void registerPostHandler(PostHandler<?> handler) {
        dispatcher.registerPostHandler(new BattlePostHandlerWrapper(battleId, handler));
    }

    public void removePostHandler(PostHandler<?> handler) {
        dispatcher.removePostHandler(new BattlePostHandlerWrapper(battleId, handler));
    }

    public <E extends BattleEvent> void handle(E event) {
        event.setBattleId(battleId);
        dispatcher.handleNow(battleId, event);
    }
}
