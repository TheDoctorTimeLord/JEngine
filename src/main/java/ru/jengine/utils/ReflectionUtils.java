package ru.jengine.utils;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectionUtils {
    public static final Predicate<Class<?>> IS_CLASS_PREDICATE =
            cls -> !cls.isAnnotation() && !cls.isInterface() && !cls.isEnum() && !cls.isRecord();

    public static Stream<Class<?>> walkBySuperclasses(@Nullable Class<?> child) {
        return child == null ? Stream.of() : Stream.iterate(child, cls -> cls.getSuperclass() != null, Class::getSuperclass);
    }
}
