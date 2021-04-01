package ru.jengine.beancontainer;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassFinder {
    void scan(String scanningPackage);
    Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation);
    Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation);
    <T> Set<Class<? extends T>> getSubclasses(Class<T> cls);
}
