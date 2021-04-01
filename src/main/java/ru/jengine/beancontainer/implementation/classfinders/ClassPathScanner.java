package ru.jengine.beancontainer.implementation.classfinders;

import org.reflections.Reflections;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathScanner implements ClassFinder {
    private Reflections reflections;

    @Override
    public void scan(String scanningPackage) {
        reflections = new Reflections(scanningPackage);
    }

    @Override
    public Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation) {
        return reflections.getTypesAnnotatedWith(annotation);
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return getAnnotatedTypes(annotation).stream()
                .filter(ClassUtils.IS_CLASS_PREDICATE)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> Set<Class<? extends T>> getSubclasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }
}
