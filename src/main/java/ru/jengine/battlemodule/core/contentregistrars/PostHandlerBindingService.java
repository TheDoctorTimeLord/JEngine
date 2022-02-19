package ru.jengine.battlemodule.core.contentregistrars;

import java.util.ArrayList;
import java.util.List;

import ru.jengine.battlemodule.core.events.BattleEvent;
import ru.jengine.battlemodule.core.events.DispatcherBattleWrapper;
import ru.jengine.eventqueue.event.PostHandler;

/**
 * Хранит информацию о зарегистрированных в бою {@link PostHandler}. Отвечает за регистрацию и удаление всех
 * добавленных через него {@link PostHandler}'ов
 */
public class PostHandlerBindingService {
    private final List<PostHandler<?>> bindings = new ArrayList<>();
    private final DispatcherBattleWrapper dispatcher;

    public PostHandlerBindingService(DispatcherBattleWrapper dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * Добавляет {@link PostHandler} и регистрирует его в {@link ru.jengine.eventqueue.Dispatcher Dispatcher}
     * @param postHandler добавляемый пост обработчик
     * @param <E> тип событий, который обрабатывает регистрируемый {@link PostHandler}
     */
    public <E extends BattleEvent> void addPostHandler(PostHandler<E> postHandler) {
        bindings.add(postHandler);
        dispatcher.registerPostHandler(postHandler);
    }

    /**
     * Удаляет все зарегистрированные {@link PostHandler}'ы из {@link ru.jengine.eventqueue.Dispatcher Dispatcher}
     */
    public void removePostHandlers() {
        bindings.forEach(dispatcher::removePostHandler);
    }
}
