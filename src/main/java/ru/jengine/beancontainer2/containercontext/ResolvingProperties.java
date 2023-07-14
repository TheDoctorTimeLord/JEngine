package ru.jengine.beancontainer2.containercontext;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class ResolvingProperties {
    private final Class<?> requestedClass;
    private Class<?> collectionClass;
    private Annotation[] annotations;
    private List<String> beanContextSource;

    public static ResolvingProperties properties(Class<?> requestedClass) {
        return new ResolvingProperties(requestedClass);
    }

    private ResolvingProperties(Class<?> requestedClass) {
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
    public List<String> getBeanContextSources() {
        return beanContextSource;
    }

    public ResolvingProperties collectionClass(Class<?> collectionClass) {
        this.collectionClass = collectionClass;
        return this;
    }

    public ResolvingProperties annotated(Annotation[] annotations) {
        this.annotations = annotations;
        return this;
    }

    public ResolvingProperties beanContextSource(List<String> beanContextSource) {
        this.beanContextSource = beanContextSource;
        return this;
    }

    public ResolvingProperties beanContextSource(String... beanContextSource) {
        return beanContextSource(Arrays.asList(beanContextSource));
    }
}
