package ru.jengine.beancontainer.utils;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
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
}
