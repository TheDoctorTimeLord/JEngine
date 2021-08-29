package ru.jengine.battlemodule.core.events;

import java.util.List;

import ru.jengine.battlemodule.core.BattleBeanPrototype;
import ru.jengine.battlemodule.core.BattleContext;

@BattleBeanPrototype
public class EventHandlerBinderService {
    private final List<EventHandlerBinder> binders;

    public EventHandlerBinderService(List<EventHandlerBinder> binders) {
        this.binders = binders;
    }

    public void bindPostHandlers(BattleContext context) {
        binders.forEach(binder -> binder.bindPostHandlerToEvent(context));
    }

    public void unbindPostHandlers(DispatcherBattleWrapper dispatcher) {
        binders.forEach(eventHandlerBinder -> eventHandlerBinder.unbindPostHandlerToEvent(dispatcher));
    }
}
