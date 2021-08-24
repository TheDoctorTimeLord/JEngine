package ru.jengine.battlemodule.core.events;

import ru.jengine.battlemodule.core.BattleContext;
import ru.jengine.eventqueue.Dispatcher;

public interface EventHandlerBinder {
    void bindPostHandlerToEvent(BattleContext battleContext);
    void unbindPostHandlerToEvent(Dispatcher dispatcher);
}
