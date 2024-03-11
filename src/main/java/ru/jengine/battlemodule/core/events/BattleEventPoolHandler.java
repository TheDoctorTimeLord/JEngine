package ru.jengine.battlemodule.core.events;

import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.fasthandling.FastComplexEventPoolHandler;

/**
 * Специальный {@link ru.jengine.eventqueue.eventpool.EventPoolHandler EventPoolHandler}, обрабатывающий события
 * сразу же после их регистрации.
 */
@Shared
public class BattleEventPoolHandler extends FastComplexEventPoolHandler {
    public BattleEventPoolHandler(String eventPoolCode) {
        super(eventPoolCode);
    }

    @Override
    public boolean isValid(Event event) {
        return event instanceof BattleEvent && getEventPoolCode().equals(((BattleEvent)event).getBattleId());
    }
}
