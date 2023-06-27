package ru.jengine.utils;

import ru.jengine.utils.hierarchywalker.HierarchyElement;
import ru.jengine.utils.hierarchywalker.WalkerIterable;
import ru.jengine.utils.hierarchywalker.WalkingException;

public class HierarchyWalkingUtils {
    public static <H> Class<?> getGenericType(Class<? extends H> startHierarchy, Class<H> targetType, int indexType) {
        for (HierarchyElement element : new WalkerIterable(startHierarchy)) {
            if (targetType.equals(element.getCurrentElement())) {
                return element.getElementTypeParameters()[indexType];
            }
        }
        throw new WalkingException("Target type [%s] is not contains in type's hierarchy [%s]".formatted(targetType, startHierarchy));
    }
}
