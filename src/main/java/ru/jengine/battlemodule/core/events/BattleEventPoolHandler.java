package ru.jengine.battlemodule.core.events;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.fasthandling.FastComplexEventPoolHandler;

public class BattleEventPoolHandler extends FastComplexEventPoolHandler {
    @Override
    public boolean isValid(Event event) {
        return event instanceof BattleEvent;
    }
}
