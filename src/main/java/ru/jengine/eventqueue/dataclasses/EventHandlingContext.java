package ru.jengine.eventqueue.dataclasses;

import java.util.List;

import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.eventqueue.EventProcessor;
import ru.jengine.utils.CollectionUtils;

public class EventHandlingContext {
    private final EventProcessor eventProcessor;
    private final List<PreHandler<?>> preHandlers;

    public EventHandlingContext(EventProcessor eventProcessor, List<PreHandler<?>> preHandlers) {
        this.eventProcessor = eventProcessor;
        this.preHandlers = preHandlers;
    }

    public EventHandlingContext(EventHandlingContext other, List<PreHandler<?>> additionalPreHandlers) {
        this(other.getEventProcessor(), CollectionUtils.concat(other.getPreHandlers(), additionalPreHandlers));
    }

    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }

    public List<PreHandler<?>> getPreHandlers() {
        return preHandlers;
    }
}
