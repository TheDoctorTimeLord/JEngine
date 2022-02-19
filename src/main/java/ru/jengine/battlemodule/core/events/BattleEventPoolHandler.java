package ru.jengine.battlemodule.core.events;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.fasthandling.FastComplexEventPoolHandler;

/**
 * Специальный {@link ru.jengine.eventqueue.eventpool.EventPoolHandler EventPoolHandler}, обрабатывающий события
 * сразу же после их регистрации.
 * //TODO Вероятно, что механизм обработки быстрых событий будет изменён после рефакторинга Dispatcher
 */
public class BattleEventPoolHandler extends FastComplexEventPoolHandler {
    @Override
    public boolean isValid(Event event) {
        return event instanceof BattleEvent;
    }
}
