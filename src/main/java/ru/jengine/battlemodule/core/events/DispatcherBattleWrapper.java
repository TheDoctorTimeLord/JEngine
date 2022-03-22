package ru.jengine.battlemodule.core.events;

import java.util.Collections;
import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleIdSetter;
import ru.jengine.eventqueue.Dispatcher;
import ru.jengine.eventqueue.EventInterceptor;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Обёртка над {@link Dispatcher}, которая обеспечивает удобную работу с диспетчером событий внутри логики боя
 */
@BattleBeanPrototype
public class DispatcherBattleWrapper implements BattleIdSetter {
    private final Dispatcher dispatcher;
    private List<EventInterceptor> eventInterceptors;
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
     */
    public void registerFastHandler() {
        BattleEventPoolHandler poolHandler = new BattleEventPoolHandler(battleId);
        eventInterceptors = Collections.singletonList(poolHandler);
        dispatcher.registerEventPoolHandler(eventInterceptors, poolHandler, Collections.emptyList());
    }

    /**
     * Удаляет обработчик быстрых событий из {@link Dispatcher}
     */
    public void removeFastHandler() {
        dispatcher.removeEventPoolHandler(eventInterceptors, battleId);
    }

    /**
     * Регистрирует {@link PostHandler} и подготавливает его для корректной регистрации в {@link Dispatcher}
     * @param handler пост обработчик событий
     */
    public <T extends BattleEvent> void registerPostHandler(PostHandler<T> handler) {
        dispatcher.registerPostHandlerToPool(battleId, handler);
    }

    /**
     * Удаляет {@link PostHandler} из {@link Dispatcher}
     * @param handler пост обработчик событий
     */
    public void removePostHandler(PostHandler<?> handler) {
        dispatcher.removePostHandlerFromPool(battleId, handler);
    }

    /**
     * Обрабатывает полученное событие из боя
     * @param event событие из боя
     * @param <E> тип события
     */
    public <E extends BattleEvent> void handle(E event) {
        event.setBattleId(battleId);
        dispatcher.registerEvent(event);
    }
}
