package ru.jengine.beancontainer.containercontext;

import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.utils.HierarchyWalkingUtils;

import javax.annotation.Nullable;
import java.util.*;

public class ClassAliasManager {
    private final Map<Class<?>, Set<Class<?>>> aliases = new HashMap<>();

    public void registerAliases(Class<?> aliased) {
        HierarchyWalkingUtils.walkThroughHierarchyIgnoreGeneric(aliased, el -> {
            Class<?> currentElement = el.getCurrentElement();
            if (aliased.equals(currentElement) || Object.class.equals(currentElement)) {
                return;
            }

            aliases.computeIfAbsent(currentElement, cls -> new HashSet<>()).add(aliased);
        });
    }

    @Nullable
    public ResolvingProperties[] getForAlias(ResolvingProperties properties) {
        Collection<Class<?>> aliased = aliases.get(properties.getRequestedClass());
        if (aliased == null) {
            return null;
        }

        return aliased.stream()
                .map(cls -> wrapClassProperties(cls, properties))
                .toArray(ResolvingProperties[]::new);
    }

    private static ResolvingProperties wrapClassProperties(Class<?> implementation, ResolvingProperties properties) {
        return ResolvingProperties.properties(implementation).fill(properties);
    }
}
