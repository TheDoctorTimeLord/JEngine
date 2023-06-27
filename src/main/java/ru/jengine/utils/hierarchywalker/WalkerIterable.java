package ru.jengine.utils.hierarchywalker;

import java.util.Iterator;

public class WalkerIterable implements Iterable<HierarchyElement> {
    private final Class<?> startHierarchy;

    public WalkerIterable(Class<?> startHierarchy) {
        this.startHierarchy = startHierarchy;
    }

    public Iterator<HierarchyElement> iterator() {
        return new WalkerIterator(startHierarchy);
    }
}
