package ru.jengine.beancontainer2.classfinders;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CompositeClassFinder implements ClassFinder {
    private final Collection<ClassFinder> classFinders;

    public CompositeClassFinder(Collection<ClassFinder> classFinders) {
        this.classFinders = classFinders;
    }

    @Override
    public void scan(String scanningPackage) { }

    @Override
    public Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation) {
        Set<Class<?>> result = new HashSet<>();
        classFinders.forEach(cf -> result.addAll(cf.getAnnotatedTypes(annotation)));
        return result;
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        Set<Class<?>> result = new HashSet<>();
        classFinders.forEach(cf -> result.addAll(cf.getAnnotatedClasses(annotation)));
        return result;
    }

    @Override
    public <T> Set<Class<? extends T>> getSubclasses(Class<T> cls) {
        Set<Class<? extends T>> result = new HashSet<>();
        classFinders.forEach(cf -> result.addAll(cf.getSubclasses(cls)));
        return result;
    }
}
