package ru.jengine.beancontainer.utils;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

public class CollectionUtils {
    public static class IterableStream <T> implements Iterable<T> {
        private final Stream<T> stream;

        public IterableStream(Stream<T> stream) {
            this.stream = stream;
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return stream.iterator();
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            stream.forEach(action);
        }

        @Override
        public Spliterator<T> spliterator() {
            return stream.spliterator();
        }
    }

    public static class ArrayAsCollection <T> extends AbstractCollection<T> {
        private final T[] array;

        public ArrayAsCollection(T[] array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() {
            return Arrays.stream(array).iterator();
        }

        @Override
        public int size() {
            return array.length;
        }
    }

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

    @SafeVarargs
    public static <V> List<V> add(List<V> elements, V... addedElements) {
        List<V> result = new ArrayList<>(elements);
        result.addAll(new ArrayAsCollection<>(addedElements));
        return result;
    }
}
