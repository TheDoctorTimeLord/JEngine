package ru.jengine.utils;

import ru.jengine.utils.hierarchywalker.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class HierarchyWalkingUtils {
    public static <H> Class<?> getGenericType(Class<? extends H> startHierarchy, Class<H> targetType, int indexType) {
        for (HierarchyElement element : new WalkerIterable(startHierarchy)) {
            if (targetType.equals(element.getCurrentElement())) {
                return element.getElementTypeParameters()[indexType];
            }
        }
        throw new WalkingException("Target type [%s] is not contains in type's hierarchy [%s]".formatted(targetType, startHierarchy));
    }

    public static void walkThroughHierarchy(Class<?> startHierarchy, Consumer<HierarchyElement> callback) {
        for (HierarchyElement element : new WalkerIterable(startHierarchy)) {
            callback.accept(element);
        }
    }

    public static void walkThroughHierarchy(Class<?> startHierarchy, Predicate<HierarchyElement> stopPredicate, Consumer<HierarchyElement> callback) {
        for (HierarchyElement element : new WalkerIterable(startHierarchy)) {
            if (stopPredicate.test(element)) {
                break;
            }

            callback.accept(element);
        }
    }

    public static void walkThroughHierarchyIgnoreGeneric(Class<?> startHierarchy, Consumer<HierarchyElement> callback) {
        WalkerIterator iterator = WalkerIteratorBuilder.builder(startHierarchy)
                .withGenericMapping(false)
                .build();

        while (iterator.hasNext()) {
            callback.accept(iterator.next());
        }
    }
}
