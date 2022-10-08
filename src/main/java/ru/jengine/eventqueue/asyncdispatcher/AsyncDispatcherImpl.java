package ru.jengine.eventqueue.asyncdispatcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.beancontainer.Constants;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;
import ru.jengine.eventqueue.exceptions.EventQueueException;

public class AsyncDispatcherImpl extends Thread implements AsyncDispatcher {
    private final Map<String, AsyncEventPoolHandler> asyncEventPoolHandlers;

    public AsyncDispatcherImpl(ConcurrentHashMap<String, AsyncEventPoolHandler> asyncEventPoolHandlers) {
        this.asyncEventPoolHandlers = asyncEventPoolHandlers;

        setName(Constants.ASYNC_DISPATCHER_NAME);
    }

    @Override
    public void registerEventPoolHandler(AsyncEventPoolHandler eventPoolHandler) {
        asyncEventPoolHandlers.put(eventPoolHandler.getEventPoolCode(), eventPoolHandler);
    }

    @Override
    public void removeEventPoolHandler(String eventPoolHandlerCode) {
        asyncEventPoolHandlers.remove(eventPoolHandlerCode);
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        asyncEventPoolHandlers.values().forEach(handler -> handler.registerPostHandler(postHandler));
    }

    @Override
    public void registerPostHandlerToPool(String eventPoolCode, PostHandler<?> postHandler) {
        getPoolHandler(eventPoolCode).registerPostHandler(postHandler);
    }

    @Override
    public void removePostHandlerFromPool(String eventPoolCode, PostHandler<?> postHandler) {
        getPoolHandler(eventPoolCode).removePostHandler(postHandler);
    }

    private AsyncEventPoolHandler getPoolHandler(String eventPoolCode) {
        AsyncEventPoolHandler asyncEventPoolHandler = asyncEventPoolHandlers.get(eventPoolCode);
        if (asyncEventPoolHandler == null) {
            throw new EventQueueException("Pool with code [%s] is not found".formatted(eventPoolCode));
        }
        return asyncEventPoolHandler;
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        asyncEventPoolHandlers.values().forEach(handler -> handler.removePostHandler(postHandler));
    }

    @Override
    public void run() {
        if (asyncEventPoolHandlers.isEmpty())
            return;

        while (true) {
            for (AsyncEventPoolHandler handler : asyncEventPoolHandlers.values()) {
                handler.handle();
            }

            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void startDispatcher() {
        setDaemon(true);
        start();
    }

    @Override
    public void stopDispatcher() throws EventQueueException {
        interrupt();
        try {
            join();
        }
        catch (InterruptedException e) {
            throw new EventQueueException("Async dispatcher was stopped but it waited to join", e);
        }
    }
}
