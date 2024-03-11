package ru.jengine.eventqueue;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.annotations.SharedBeansProvider;
import ru.jengine.eventqueue.asyncdispatcher.AsyncDispatcher;
import ru.jengine.eventqueue.asyncdispatcher.AsyncDispatcherImpl;
import ru.jengine.eventqueue.asyncdispatcher.StubAsyncDispatcher;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.EventRegistrar;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPool;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;
import ru.jengine.eventqueue.exceptions.EventQueueException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Bean
public class Dispatcher implements EventPoolProvider, PollableEventHandlerRegistrar, EventRegistrar,
        SynchronousEventHandler, EventPoolHandlersManager
{
    private final Map<String, EventPool> eventPoolByCode = new ConcurrentHashMap<>();
    private Map<String, EventPoolHandler> eventPoolHandlers;
    private List<EventInterceptor> interceptors;
    private AsyncDispatcher asyncDispatcher;
    private EventHandlingContext mainEventHandlingContext;

    @SharedBeansProvider
    private void initializeDispatcher(List<EventInterceptor> interceptors, List<EventPoolHandler> eventPoolHandlers,
                      List<PreHandler<?>> commonPreHandlers)
    {
        this.interceptors = interceptors;
        this.mainEventHandlingContext = new EventHandlingContext(new BaseEventProcessor(), commonPreHandlers);

        eventPoolHandlers.forEach(handler -> initializeEventPoolHandler(handler, mainEventHandlingContext));

        this.eventPoolHandlers = new ConcurrentHashMap<>(eventPoolHandlers.stream()
                .filter(handler -> !(handler instanceof AsyncEventPoolHandler))
                .collect(Collectors.toMap(EventPoolHandler::getEventPoolCode, handler -> handler)));

        ConcurrentHashMap<String, AsyncEventPoolHandler> asyncEventPoolHandlers =
                new ConcurrentHashMap<>(eventPoolHandlers.stream()
                        .filter(AsyncEventPoolHandler.class::isInstance)
                        .collect(Collectors.toMap(EventPoolHandler::getEventPoolCode, AsyncEventPoolHandler.class::cast)));

        this.asyncDispatcher = asyncEventPoolHandlers.isEmpty()
                ? new StubAsyncDispatcher()
                : new AsyncDispatcherImpl(asyncEventPoolHandlers);
        this.asyncDispatcher.startDispatcher();
    }

    @Override
    public void handleEvents() {
        eventPoolHandlers.values().forEach(EventPoolHandler::handle);
    }

    @Override
    public void handleEvents(String eventPoolCode) {
        EventPoolHandler eventPoolHandler = eventPoolHandlers.get(eventPoolCode);
        if (eventPoolHandler == null) {
            throw new EventQueueException("Pool with code [%s] is not found".formatted(eventPoolCode));
        }
        eventPoolHandler.handle();
    }

    @Override
    public void registerEventPoolHandler(List<EventInterceptor> eventInterceptors, EventPoolHandler eventPoolHandler,
            List<PreHandler<?>> additionalPreHandlers)
    {
        //TODO сейчас при добавлении, пул хендлер не получает уже зарегистрированные общие пост хендлеры
        initializeEventPoolHandler(eventPoolHandler, new EventHandlingContext(mainEventHandlingContext, additionalPreHandlers));

        synchronized (interceptors) {
            interceptors.addAll(eventInterceptors);
        }

        if (eventPoolHandler instanceof AsyncEventPoolHandler asyncEventPoolHandler) {
            asyncDispatcher.registerEventPoolHandler(asyncEventPoolHandler);
        } else {
            eventPoolHandlers.put(eventPoolHandler.getEventPoolCode(), eventPoolHandler);
        }
    }

    @Override
    public void removeEventPoolHandler(List<EventInterceptor> eventInterceptors, String eventPoolHandlerCode) {
        synchronized (interceptors) {
            interceptors.removeAll(eventInterceptors);
        }

        eventPoolByCode.remove(eventPoolHandlerCode);

        if (eventPoolHandlers.containsKey(eventPoolHandlerCode)) {
            eventPoolHandlers.remove(eventPoolHandlerCode); //Здесь нам не важно, успел ли кто-то удалить пул до этого
            return;
        }

        asyncDispatcher.removeEventPoolHandler(eventPoolHandlerCode);
    }

    private void initializeEventPoolHandler(EventPoolHandler handler, EventHandlingContext context) {
        EventPool eventPool = handler.initialize(context);
        String eventPoolCode = handler.getEventPoolCode();

        if (eventPoolCode != null && eventPool != null) {
            synchronized (eventPoolByCode) {
                if (eventPoolByCode.containsKey(eventPoolCode)) {
                    String errorMessage = "EventPool with code [%s] was registered already. Pool handler [%s] is not registered"
                            .formatted(eventPoolCode, handler);
                    throw new EventQueueException(errorMessage);
                }
                eventPoolByCode.put(eventPoolCode, eventPool);
            }
        }
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        eventPoolHandlers.values().forEach(handler -> handler.registerPostHandler(postHandler));
        asyncDispatcher.registerPostHandler(postHandler);
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        eventPoolHandlers.values().forEach(handler -> handler.removePostHandler(postHandler));
        asyncDispatcher.removePostHandler(postHandler);
    }

    @Override
    public void registerPostHandlerToPool(String eventPoolCode, PostHandler<?> postHandler) {
        EventPoolHandler poolHandler = eventPoolHandlers.get(eventPoolCode);
        if (poolHandler != null) {
            poolHandler.registerPostHandler(postHandler);
        } else {
            asyncDispatcher.registerPostHandlerToPool(eventPoolCode, postHandler);
        }
    }

    @Override
    public void removePostHandlerFromPool(String eventPoolCode, PostHandler<?> postHandler) {
        EventPoolHandler poolHandler = eventPoolHandlers.get(eventPoolCode);
        if (poolHandler != null) {
            poolHandler.removePostHandler(postHandler);
        } else {
            asyncDispatcher.removePostHandlerFromPool(eventPoolCode, postHandler);
        }
    }

    @Override
    @Nullable
    public EventPool getEventPool(String eventPoolCode) {
        return eventPoolByCode.get(eventPoolCode);
    }

    @Override
    public <E extends Event> void registerEvent(E event) {
        synchronized (interceptors) { //TODO изменить синхронизацию, сейчас она медленная
            for (EventInterceptor interceptor : interceptors) {
                if (interceptor.isValid(event)) {
                    interceptor.intercept(event, this);
                }
            }
        }
        synchronized (asyncDispatcher) {
            asyncDispatcher.notify();
        }
    }

    @PreDestroy
    public void stopDispatcher() {
        asyncDispatcher.stopDispatcher();
    }
}
