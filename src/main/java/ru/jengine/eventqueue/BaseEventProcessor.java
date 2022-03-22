package ru.jengine.eventqueue;

import java.util.Collection;
import java.util.List;

import ru.jengine.beancontainer.service.SortedMultiset;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.EventHandler;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;

public class BaseEventProcessor implements EventProcessor {
    @Override
    public void process(List<PreHandler<Event>> preHandlers, SortedMultiset<PostHandler<Event>> postHandlers, Event event)
    {
        if (preHandlers.isEmpty() && postHandlers.isEmpty()) {
            return;
        }

        Collection<PostHandler<Event>> postHandlersForEvent = postHandlers.getSortedElements();

        //Валидация полученных событий
        for (PreHandler<Event> handler : preHandlers) {
            if (!handler.isValid(event))
                return;
        }

        //Полная обработка событий
        for (EventHandler<Event> handler : preHandlers) {
            handler.handle(event);
        }

        for (EventHandler<Event> handler : postHandlersForEvent) {
            handler.handle(event);
        }
    }
}
