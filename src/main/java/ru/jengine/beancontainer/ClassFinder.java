package ru.jengine.beancontainer;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassFinder {
    void scan(String packages);
    Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation);
}
