package ru.jengine.utils.hierarchywalker;

public interface HierarchyElement {
    Class<?> getCurrentElement();
    Class<?>[] getElementTypeParameters();
}
