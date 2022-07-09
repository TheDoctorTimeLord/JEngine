package ru.jengine.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class ReflectionUtils {
    public static final Predicate<Class<?>> IS_CLASS_PREDICATE = cls -> !cls.isAnnotation() && !cls.isInterface();

    public static boolean hasInterface(Class<?> cls, Class<?> presentInterface) {
        return Stream.of(cls.getInterfaces()).anyMatch(presentInterface::isAssignableFrom);
    }

    @Nullable
    public static Class<?> getGenericHierarchyInterfaceType(Class<?> mainClass, Class<?> availableType, int index) {
        LazyGenericTypesIterable iterable = new LazyGenericTypesIterable(mainClass);
        return ReflectionUtils.getGenericInterfaceType(iterable, availableType, index);
    }

    @Nullable
    public static Class<?> getGenericInterfaceType(LazyHierarchyIterable iterable, Class<?> availableType, int index) {
        for (Type genericInterface : iterable) {
            if (genericInterface instanceof ParameterizedType type && type.getRawType().equals(availableType))
            {
                Type[] actualTypes = type.getActualTypeArguments();
                if (0 <= index && index < actualTypes.length) {
                    Type actualType = actualTypes[index];
                    if (!(actualType instanceof TypeVariable<?>)) {
                        return (Class<?>)actualType;
                    }
                }
            }
        }
        return null;
    }

    public static class LazyGenericTypesIterable extends LazyHierarchyIterable {
        private static final Function<Type, Iterator<Type>> MAPPER = type -> {
            if (!(type instanceof Class<?> cls)) {
                return Collections.emptyIterator();
            }

            return Stream.concat(Stream.of(cls.getGenericSuperclass()), Arrays.stream(cls.getGenericInterfaces()))
                    .filter(Objects::nonNull)
                    .toList()
                    .iterator();
        };

        public LazyGenericTypesIterable(Class<?> rootClass) {
            super(rootClass, MAPPER);
        }
    }

    public static class LazyHierarchyIterable implements Iterable<Type> {
        private final Class<?> rootClass;
        private final Function<Type, Iterator<Type>> mapper;

        public LazyHierarchyIterable(Class<?> rootClass, Function<Type, Iterator<Type>> mapper) {
            this.rootClass = rootClass;
            this.mapper = mapper;
        }

        @NotNull
        @Override
        public Iterator<Type> iterator() {
            return new LazyHierarchyIterator(rootClass, mapper);
        }
    }

    public static class LazyHierarchyIterator implements Iterator<Type> {
        private final Function<Type, Iterator<Type>> mapper;
        private Class<?> currentClass;
        private Iterator<Type> mappedIterator;

        public LazyHierarchyIterator(Class<?> currentClass, Function<Type, Iterator<Type>> mapper) {
            this.mapper = mapper;
            this.currentClass = currentClass;

            prepareIterator();
        }

        @Override
        public boolean hasNext() {
            while (!currentClass.equals(Object.class)) {
                if (mappedIterator.hasNext()) {
                    return true;
                }
                currentClass = currentClass.getSuperclass();
                prepareIterator();
            }

            return false;
        }

        @Override
        public Type next() {
            return mappedIterator.next();
        }

        private void prepareIterator() {
            mappedIterator = mapper.apply(currentClass);
        }
    }
}
