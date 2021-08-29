package ru.jengine.battlemodule.core.events;

import ru.jengine.battlemodule.core.BattleContext;

public interface EventHandlerBinder {
    void bindPostHandlerToEvent(BattleContext battleContext);
    void unbindPostHandlerToEvent(DispatcherBattleWrapper dispatcher);
}
