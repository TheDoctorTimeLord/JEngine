package ru.jengine.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReflectionUtils {
    public static final Predicate<Class<?>> IS_CLASS_PREDICATE = cls -> !cls.isAnnotation() && !cls.isInterface();

    public static boolean hasInterface(Class<?> cls, Class<?> presentInterface) {
        return Stream.of(cls.getInterfaces()).anyMatch(presentInterface::isAssignableFrom);
    }
}
