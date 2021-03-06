package ru.jengine.beancontainer.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SortedMultimap<E> {
    private final SortedMap<Integer, List<E>> container;
    private final Function<E, Integer> priorityExtractor;

    public SortedMultimap() {
        this(element -> 0);
    }

    public SortedMultimap(Function<E, Integer> priorityExtractor) {
        this(Comparator.naturalOrder(), priorityExtractor);
    }

    public SortedMultimap(Comparator<Integer> comparator, Function<E, Integer> priorityExtractor) {
        this.container = new TreeMap<>(comparator);
        this.priorityExtractor = priorityExtractor;
    }

    public void add(E element) {
        Integer priority = priorityExtractor.apply(element);
        List<E> elements = container.computeIfAbsent(priority, p -> new LinkedList<>());
        elements.add(element);
    }

    public void remove(E element) {
        Integer priority = priorityExtractor.apply(element);
        List<E> elements = container.get(priority);

        if (elements != null && !elements.isEmpty()) {
            elements.remove(element);
        }
    }

    public Collection<E> getSortedElements() {
        return container.values().stream()
                .filter(list -> !list.isEmpty())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }
}
