package ru.jengine.beancontainer2.containercontext;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public class ResolvingPropertyDefinition implements ResolvingProperties {
    private final Class<?> requestedClass;
    private Class<?> collectionClass;
    private Annotation[] annotations;
    private String[] beanContextSource;

    public static ResolvingPropertyDefinition properties(Class<?> requestedClass) {
        return new ResolvingPropertyDefinition(requestedClass);
    }

    private ResolvingPropertyDefinition(Class<?> requestedClass) {
        this.requestedClass = requestedClass;
    }

    public Class<?> getRequestedClass() {
        return requestedClass;
    }

    @Nullable
    public Class<?> getCollectionClass() {
        return collectionClass;
    }

    @Nullable
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Nullable
    public String[] getBeanContextSources() {
        return beanContextSource;
    }

    public ResolvingPropertyDefinition collectionClass(Class<?> collectionClass) {
        this.collectionClass = collectionClass;
        return this;
    }

    public ResolvingPropertyDefinition annotated(Annotation[] annotations) {
        this.annotations = annotations;
        return this;
    }

    public ResolvingPropertyDefinition beanContextSource(String... beanContextSource) {
        this.beanContextSource = beanContextSource;
        return this;
    }

    public ResolvingPropertyDefinition fill(ResolvingProperties other) {
        return collectionClass(other.getCollectionClass())
                .annotated(other.getAnnotations())
                .beanContextSource(other.getBeanContextSources());
    }
}
