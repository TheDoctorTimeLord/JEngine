package ru.jengine.beancontainer2.statepublisher;

import ru.jengine.beancontainer2.ContainerState;

import java.util.*;

public class ContainerStatePublisher {
    private final Map<Class<?>, Set<ContainerListener<?>>> listeners = new HashMap<>();

    public <E extends ContainerEvent> void registerListener(ContainerListener<E> eventListener) {
        listeners.computeIfAbsent(eventListener.getListenedEventClass(), c -> new HashSet<>()).add(eventListener);
    }

    public void removeListener(ContainerListener<?> eventListener) {
        listeners.getOrDefault(eventListener.getListenedEventClass(), Collections.emptySet()).remove(eventListener);
    }

    @SuppressWarnings("unchecked")
    public void publish(ContainerEvent event, ContainerState containerState) {
        for (ContainerListener<?> listener : listeners.getOrDefault(event.getClass(), Collections.emptySet())) {
            ((ContainerListener<ContainerEvent>)listener).handle(event, containerState);
        }
    }
}
