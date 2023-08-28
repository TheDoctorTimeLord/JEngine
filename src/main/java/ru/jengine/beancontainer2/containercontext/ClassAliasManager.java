package ru.jengine.beancontainer2.containercontext;

import ru.jengine.utils.HierarchyWalkingUtils;

import javax.annotation.Nullable;
import java.util.*;

public class ClassAliasManager {
    private final Map<Class<?>, Set<Class<?>>> aliases = new HashMap<>();

    public void registerAliases(Class<?> aliased) {
        HierarchyWalkingUtils.walkThroughHierarchy(aliased, el -> {
            Class<?> currentElement = el.getCurrentElement();
            if (currentElement.equals(aliased)) {
                return;
            }

            aliases.computeIfAbsent(currentElement, cls -> new HashSet<>()).add(aliased);
        });
    }

    @Nullable
    public ResolvingPropertyDefinition[] getForAlias(ResolvingProperties properties) {
        Collection<Class<?>> aliased = aliases.get(properties.getRequestedClass());
        if (aliased == null) {
            return null;
        }

        return wrapClassAliases(aliased, properties);
    }

    private static ResolvingPropertyDefinition[] wrapClassAliases(Collection<Class<?>> aliased, ResolvingProperties properties) {
        return aliased.stream()
                .map(cls -> wrapClassProperties(cls, properties))
                .toArray(ResolvingPropertyDefinition[]::new);
    }

    private static ResolvingPropertyDefinition wrapClassProperties(Class<?> implementation, ResolvingProperties properties) {
        return ResolvingProperties.properties(implementation).fill(properties);
    }
}
