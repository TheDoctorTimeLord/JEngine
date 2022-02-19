package ru.jengine.battlemodule.core.events;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleIdSetter;
import ru.jengine.eventqueue.Dispatcher;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;

/**
 * Обёртка над {@link Dispatcher}, которая обеспечивает удобную работу с диспетчером событий внутри логики боя
 */
@BattleBeanPrototype
public class DispatcherBattleWrapper implements BattleIdSetter {
    private final Dispatcher dispatcher;
    private String battleId;

    public DispatcherBattleWrapper(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void setBattleId(String battleCode) {
        this.battleId = battleCode;
    }

    /**
     * Регистрирует обработчик очереди для быстрых событий
     * @param interceptors список перехватчиков событий для передаваемого обработчика
     * @param handler обработчик очереди для быстрых событий
     */
    public void registerFastHandler(List<EventInterceptor> interceptors, EventPoolHandler handler) {
        dispatcher.registerFastHandler(battleId, interceptors, handler);
    }

    /**
     * Удаляет обработчик быстрых событий из {@link Dispatcher}
     */
    public void removeFastHandler() {
        dispatcher.removeFastHandler(battleId);
    }

    /**
     * Регистрирует {@link PostHandler} и подготавливает его для корректной регистрации в {@link Dispatcher}
     * @param handler пост обработчик событий
     */
    public <T extends BattleEvent> void registerPostHandler(PostHandler<T> handler) {
        dispatcher.registerPostHandler(new BattlePostHandlerWrapper<>(battleId, handler));
    }

    /**
     * Удаляет {@link PostHandler} из {@link Dispatcher}
     * @param handler пост обработчик событий
     */
    public void removePostHandler(PostHandler<?> handler) {
        dispatcher.removePostHandler(new BattlePostHandlerWrapper(battleId, handler));
    }

    /**
     * Обрабатывает полученное событие из боя
     * @param event событие из боя
     * @param <E> тип события
     */
    public <E extends BattleEvent> void handle(E event) {
        event.setBattleId(battleId);
        dispatcher.handleNow(battleId, event);
    }
}
