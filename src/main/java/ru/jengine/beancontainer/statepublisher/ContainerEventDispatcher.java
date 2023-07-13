package ru.jengine.beancontainer.statepublisher;

import ru.jengine.beancontainer.ContainerState;

import java.util.*;

public class ContainerEventDispatcher {
    private final Map<Class<?>, Set<ContainerListener<?>>> listeners = new HashMap<>();

    public <E extends ContainerEvent> void registerListener(ContainerListener<E> eventListener) {
        listeners.computeIfAbsent(eventListener.getListenedEventClass(), c -> new HashSet<>()).add(eventListener);
    }

    public void removeListener(ContainerListener<?> eventListener) {
        listeners.getOrDefault(eventListener.getListenedEventClass(), Collections.emptySet()).remove(eventListener);
    }

    @SuppressWarnings("unchecked")
    public void publish(ContainerEvent event, ContainerState containerState) {
        for (ContainerListener<?> listener : getAvailableListeners(event.getClass())) {
            ((ContainerListener<ContainerEvent>)listener).handle(event, containerState);
        }
    }

    private Collection<ContainerListener<?>> getAvailableListeners(Class<?> eventClass) {
        Set<ContainerListener<?>> containerListeners = listeners.get(eventClass);
        return containerListeners != null ? new ArrayList<>(containerListeners) : Collections.emptySet();
    }
}
