package ru.jengine.utils.serviceclasses;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;

public class SortedMultiset<E> {
    private final SortedMap<Integer, List<E>> container;
    private final Function<E, Integer> priorityExtractor;

    public SortedMultiset() {
        this(element -> 0);
    }

    public SortedMultiset(Function<E, Integer> priorityExtractor) {
        this(Comparator.naturalOrder(), priorityExtractor);
    }

    public SortedMultiset(Comparator<Integer> comparator, Function<E, Integer> priorityExtractor) {
        this.container = new TreeMap<>(comparator);
        this.priorityExtractor = priorityExtractor;
    }

    public void add(E element) {
        Integer priority = priorityExtractor.apply(element);
        List<E> elements = container.computeIfAbsent(priority, p -> new LinkedList<>());
        elements.add(element);
    }

    public void addAll(Collection<E> elements) {
        for (E element : elements) {
            add(element);
        }
    }

    public void addAll(SortedMultiset<E> otherMultiset) {
        addAll(otherMultiset.getSortedElements());
    }

    public void remove(E element) {
        Integer priority = priorityExtractor.apply(element);
        List<E> elements = container.get(priority);

        if (elements != null && !elements.isEmpty()) {
            elements.remove(element);
        }
    }

    public List<E> getSortedElements() {
        return container.values().stream()
                .filter(list -> !list.isEmpty())
                .flatMap(Collection::stream)
                .toList();
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }
}
