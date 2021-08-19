package ru.jengine.battlemodule.events;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.eventqueue.Dispatcher;

public interface EventHandlerBinder {
    void bindPostHandlerToEvent(BattleContext battleContext);
    void unbindPostHandlerToEvent(Dispatcher dispatcher);
}
