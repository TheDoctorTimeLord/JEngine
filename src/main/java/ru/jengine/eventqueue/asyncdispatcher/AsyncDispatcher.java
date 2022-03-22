package ru.jengine.eventqueue.asyncdispatcher;

import ru.jengine.eventqueue.AsyncEventPoolHandlersManager;
import ru.jengine.eventqueue.PollableEventHandlerRegistrar;
import ru.jengine.eventqueue.exceptions.EventQueueException;

public interface AsyncDispatcher extends PollableEventHandlerRegistrar, AsyncEventPoolHandlersManager {
    void startDispatcher();
    void stopDispatcher() throws EventQueueException;
}
