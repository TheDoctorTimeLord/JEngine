package ru.jengine.eventqueue;

import java.util.List;

import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.EventHandler;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.utils.serviceclasses.SortedMultiset;

public class BaseEventProcessor implements EventProcessor {
    @Override
    public void process(List<PreHandler<Event>> preHandlers, List<PostHandler<Event>> postHandlers, Event event)
    {
        if (preHandlers.isEmpty() && postHandlers.isEmpty()) {
            return;
        }

        //Валидация полученных событий
        for (PreHandler<Event> handler : preHandlers) {
            if (!handler.isValid(event))
                return;
        }

        //Полная обработка событий
        for (EventHandler<Event> handler : preHandlers) {
            handler.handle(event);
        }

        for (EventHandler<Event> handler : postHandlers) {
            handler.handle(event);
        }
    }
}
