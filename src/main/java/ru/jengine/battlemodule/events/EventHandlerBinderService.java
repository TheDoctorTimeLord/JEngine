package ru.jengine.battlemodule.events;

import java.util.List;

import ru.jengine.battlemodule.BattleContext;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;
import ru.jengine.eventqueue.Dispatcher;

@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public class EventHandlerBinderService {
    private final List<EventHandlerBinder> binders;

    public EventHandlerBinderService(List<EventHandlerBinder> binders) {
        this.binders = binders;
    }

    public void bindPostHandlers(BattleContext context) {
        binders.forEach(binder -> binder.bindPostHandlerToEvent(context));
    }

    public void unbindPostHandlers(Dispatcher dispatcher) {
        binders.forEach(eventHandlerBinder -> eventHandlerBinder.unbindPostHandlerToEvent(dispatcher));
    }
}
