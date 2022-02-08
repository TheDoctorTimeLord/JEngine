package ru.jengine.eventqueue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.service.HasPriority;
import ru.jengine.beancontainer.service.SortedMultimap;
import ru.jengine.eventqueue.dataclasses.EventHandlingContext;
import ru.jengine.eventqueue.event.Event;
import ru.jengine.eventqueue.event.EventHandler;
import ru.jengine.eventqueue.event.EventRegistrar;
import ru.jengine.eventqueue.event.PostHandler;
import ru.jengine.eventqueue.event.PreHandler;
import ru.jengine.eventqueue.eventpool.AsyncEventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPool;
import ru.jengine.eventqueue.eventpool.EventPoolHandler;
import ru.jengine.eventqueue.eventpool.EventPoolProvider;
import ru.jengine.eventqueue.exceptions.EventQueueException;
import ru.jengine.eventqueue.fasthandling.FastEventHandler;
import ru.jengine.eventqueue.fasthandling.FastEventHandlerImpl;

@Bean
public class Dispatcher implements EventPoolProvider, EventHandlerRegistrar, EventRegistrar, SynchronousEventHandler,
        FastEventHandler //TODO порафакторить диспетчер на предмет Single Responsibility
{
    private final Map<Class<?>, List<PreHandler<?>>> preHandlers = new HashMap<>();
    private final Map<Class<?>, SortedMultimap<PostHandler<?>>> postHandlers = new ConcurrentHashMap<>();
    private final Map<String, EventPool> eventPoolByCode = new ConcurrentHashMap<>();
    private final FastEventHandler fastEventHandlerDelegate;
    private final EventHandlingContext handlingContext;
    private final List<EventInterceptor> interceptors;
    private final List<EventPoolHandler> eventPoolHandlers;
    private final List<AsyncEventPoolHandler> asyncEventPoolHandlers;
    private AsyncDispatcher asyncDispatcher;

    public Dispatcher(List<EventInterceptor> interceptors, List<EventPoolHandler> eventPoolHandlers,
                      List<PreHandler<?>> preHandlers)
    {
        this.fastEventHandlerDelegate = new FastEventHandlerImpl(this);
        this.handlingContext = new EventHandlingContext(this::handle);
        this.interceptors = interceptors;
        this.eventPoolHandlers = eventPoolHandlers.stream()
                .filter(handler -> !(handler instanceof AsyncEventPoolHandler))
                .collect(Collectors.toList());
        this.asyncEventPoolHandlers = eventPoolHandlers.stream()
                .filter(handler -> handler instanceof AsyncEventPoolHandler)
                .map(handler -> (AsyncEventPoolHandler) handler)
                .collect(Collectors.toList());
        preHandlers.forEach(this::registerPreHandler);
    }

    @PostConstruct
    private void initialize() {
        eventPoolHandlers.forEach(handler -> initializeEventPoolHandler(handler, handlingContext));
        asyncEventPoolHandlers.forEach(handler -> initializeEventPoolHandler(handler, handlingContext));
        initializeAsyncDispatcher();
    }

    @Override
    public void registerFastHandler(String handlerCode, List<EventInterceptor> interceptors,
            EventPoolHandler handler)
    {
        initializeEventPoolHandler(handler, handlingContext);
        fastEventHandlerDelegate.registerFastHandler(handlerCode, interceptors, handler);
    }

    @Override
    public EventPoolHandler removeFastHandler(String handlerCode) {
        EventPoolHandler handler = fastEventHandlerDelegate.removeFastHandler(handlerCode);
        if (handler != null) {
            cleanAfterFastHandler(handler);
            return handler;
        }
        return null;
    }

    @Override
    public <E extends Event> void handleNow(String handlerCode, E event) {
        fastEventHandlerDelegate.handleNow(handlerCode, event);
    }

    private void initializeEventPoolHandler(EventPoolHandler handler, EventHandlingContext context) {
        EventPool eventPool = handler.initialize(context);
        String eventPoolCode = handler.getEventPoolCode();

        if (eventPoolCode != null) {
            synchronized (eventPoolByCode) {
                if (eventPoolByCode.containsKey(eventPoolCode)) {
                    throw new EventQueueException("EventPool with code [" + eventPoolCode + "] was registered already. "
                                    + "Problem with event pool handler [" + handler + "]");
                }
                eventPoolByCode.put(eventPoolCode, eventPool);
            }
        }
    }

    private void cleanAfterFastHandler(EventPoolHandler handler) {
        synchronized (eventPoolByCode) {
            String eventPoolCode = handler.getEventPoolCode();
            if (eventPoolCode != null) {
                eventPoolByCode.remove(eventPoolCode);
            }
        }
    }

    private void initializeAsyncDispatcher() {
        asyncDispatcher = new AsyncDispatcher(asyncEventPoolHandlers);
        asyncDispatcher.setDaemon(true);
        asyncDispatcher.start();
    }

    private void registerPreHandler(PreHandler<?> preHandler) {
        Class<?> handledEventType = preHandler.getHandlingEventType();
        List<PreHandler<?>> handlers = preHandlers.computeIfAbsent(handledEventType, k -> new ArrayList<>());
        handlers.add(preHandler);
    }

    @Override
    public void registerPostHandler(PostHandler<?> postHandler) {
        synchronized (postHandlers) {
            Class<?> handledEventType = postHandler.getHandlingEventType();
            SortedMultimap<PostHandler<?>> handlers = postHandlers.computeIfAbsent(handledEventType,
                    k -> new SortedMultimap<>(HasPriority::getPriority));
            handlers.add(postHandler);
        }
    }

    @Override
    public void removePostHandler(PostHandler<?> postHandler) {
        synchronized (postHandlers) {
            Class<?> handledEventType = postHandler.getHandlingEventType();
            SortedMultimap<PostHandler<?>> handlers = postHandlers.get(handledEventType);
            if (handlers == null) {
                return;
            }

            handlers.remove(postHandler);

            if (handlers.isEmpty()) {
                postHandlers.remove(handledEventType);
            }
        }
    }

    private Collection<PostHandler<?>> getPostHandlers(Class<?> eventType) {
        synchronized (postHandlers) {
            return postHandlers.getOrDefault(eventType, new SortedMultimap<>()).getSortedElements();
        }
    }

    @Override
    public EventPool getEventPool(String eventPoolCode) {
        EventPool pool = eventPoolByCode.get(eventPoolCode);

        if (pool == null) {
            throw new EventQueueException("Not found event pool [" + eventPoolCode + "]");
        }

        return pool;
    }

    @Override
    public <E extends Event> void registerEvent(E event) {
        for (EventInterceptor interceptor : interceptors) {
            if (interceptor.isValid(event)) {
                interceptor.intercept(event, this);
            }
        }
    }

    @Override
    public void handleEvents() {
        eventPoolHandlers.forEach(EventPoolHandler::handle);
    }

    private void handle(Event event) {
        Class<?> eventType = event.getClass();
        if (!preHandlers.containsKey(eventType) && !postHandlers.containsKey(eventType))
            return;

        List<PreHandler<?>> preHandlersForEvent = preHandlers.getOrDefault(eventType, Collections.emptyList());
        for (PreHandler handler : preHandlersForEvent) {
            if (!handler.isValid(event))
                return;
        }

        Collection<PostHandler<?>> postHandlersForEvent = getPostHandlers(eventType);

        for (EventHandler handler : preHandlersForEvent) {
            handler.handle(event);
        }

        for (EventHandler handler : postHandlersForEvent) {
            handler.handle(event);
        }
    }

    @PreDestroy
    public void stopDispatcher() {
        asyncDispatcher.stopDispatcher();
    }

    private static class AsyncDispatcher extends Thread {
        private final List<AsyncEventPoolHandler> asyncEventPoolHandlers;
        private volatile boolean isRunning = true;

        public AsyncDispatcher(List<AsyncEventPoolHandler> asyncEventPoolHandlers) {
            this.asyncEventPoolHandlers = asyncEventPoolHandlers;

            setName(Constants.DISPATCHER_ASYNC_NAME);
        }

        @Override
        public void run() {
            if (asyncEventPoolHandlers.isEmpty())
                return;

            while (isRunning) {
                for (AsyncEventPoolHandler handler : asyncEventPoolHandlers) {
                    handler.handle();
                }

                try {
                    Thread.sleep(1); //TODO подумать на счёт остановки
                } catch (InterruptedException e) {
                    isRunning = false;
                }
            }
        }

        public void stopDispatcher() {
            isRunning = false;
            try {
                join();
            }
            catch (InterruptedException e) {
                throw new EventQueueException("Async dispatcher was stopped but it waited to join", e);
            }
        }
    }
}
