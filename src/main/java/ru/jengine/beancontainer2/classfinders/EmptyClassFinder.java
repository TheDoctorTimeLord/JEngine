package ru.jengine.beancontainer2.classfinders;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class EmptyClassFinder implements ClassFinder {
    public static final EmptyClassFinder INSTANCE = new EmptyClassFinder();

    private EmptyClassFinder() {}

    @Override
    public void scan(String scanningPackage) { }

    @Override
    public Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation) {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return Collections.emptySet();
    }

    @Override
    public <T> Set<Class<? extends T>> getSubclasses(Class<T> cls) {
        return Collections.emptySet();
    }
}
