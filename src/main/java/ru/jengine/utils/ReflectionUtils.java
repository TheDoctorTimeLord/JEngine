package ru.jengine.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectionUtils {
    public static final Predicate<Class<?>> IS_CLASS_PREDICATE =
            cls -> !cls.isAnnotation() && !cls.isInterface() && !cls.isEnum() && !cls.isRecord();

    public static Stream<Class<?>> walkBySuperclasses(Class<?> child) {
        return Stream.iterate(child, cls -> cls.getSuperclass() != null, Class::getSuperclass);
    }
}
