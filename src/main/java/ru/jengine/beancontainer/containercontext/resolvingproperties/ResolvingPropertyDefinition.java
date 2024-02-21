package ru.jengine.beancontainer.containercontext.resolvingproperties;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ResolvingPropertyDefinition implements ResolvingProperties {
    private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

    private final Class<?> requestedClass;
    private Class<?> collectionClass;
    private Annotation[] annotations;
    private String[] beanContextSource;

    ResolvingPropertyDefinition(Class<?> requestedClass) {
        this.requestedClass = requestedClass;
    }

    @Override
    public Class<?> getRequestedClass() {
        return requestedClass;
    }

    @Nullable
    @Override
    public Class<?> getCollectionClass() {
        return collectionClass;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations != null ? annotations : EMPTY_ANNOTATIONS;
    }

    @Nullable
    @Override
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

    @Override
    public String toString() {
        return "ResolvingPropertyDefinition{" +
                "requestedClass=" + requestedClass +
                ", collectionClass=" + collectionClass +
                ", annotations=" + Arrays.toString(annotations) +
                ", beanContextSource=" + Arrays.toString(beanContextSource) +
                '}';
    }
}
