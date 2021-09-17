package ru.jengine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils {
    public static <E> List<E> filter(List<E> elements, Predicate<E> filter) {
        List<E> filtered = new ArrayList<>();

        elements.removeIf(module -> {
            if (filter.test(module)) {
                filtered.add(module);
                return true;
            }
            return false;
        });

        return filtered;
    }

    public static <K, V> Map<K, List<V>> groupBy(List<V> elements, Function<V, K> keyExtractor) {
        Map<K, List<V>> result = new HashMap<>();

        for (V element : elements) {
            K key = keyExtractor.apply(element);
            if (!result.containsKey(key)) {
                result.put(key, new ArrayList<>());
            }
            result.get(key).add(element);
        }

        return result;
    }

    public static <V> List<V> concat(Object... elements) {
        List<V> result = new ArrayList<>();

        for (Object element : elements) {
            if (element instanceof Collection) {
                result.addAll((Collection<? extends V>)element);
            } else {
                result.add((V)element);
            }
        }

        return result;
    }

    public static <V> V getLast(List<V> elements) {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }
}
