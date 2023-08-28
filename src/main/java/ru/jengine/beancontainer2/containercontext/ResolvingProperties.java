package ru.jengine.beancontainer2.containercontext;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface ResolvingProperties {
    Class<?> getRequestedClass();

    @Nullable
    Class<?> getCollectionClass();

    @Nullable
    Annotation[] getAnnotations();

    @Nullable
    String[] getBeanContextSources();

    static ResolvingPropertyDefinition properties(Class<?> requestedClass) {
        return new ResolvingPropertyDefinition(requestedClass);
    }
}
