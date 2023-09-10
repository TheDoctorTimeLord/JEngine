package ru.jengine.beancontainer.containercontext;

import javax.annotation.Nullable;

public interface BeanExtractor {
    @Nullable
    Object getBean(ResolvingProperties properties);

    Object NOT_RESOLVED = new Object();

    static boolean isResolved(Object resolvedValue) {
        return resolvedValue != NOT_RESOLVED;
    }
}
