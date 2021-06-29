package ru.jengine.beancontainer.implementation.classfinders;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.utils.ClassUtils;

public class ClassPathScanner implements ClassFinder {
    private Reflections reflections;

    @Override
    public void scan(String scanningPackage) {
        reflections = new Reflections(ConfigurationBuilder
                .build(scanningPackage)
                .setScanners(new TypeAnnotationsScannerExtensions(), new SubTypesScanner())
        );
    }

    @Override
    public Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation) {
        Collection<String> annotationNames = reflections.getStore()
                .get(TypeAnnotationsScannerExtensions.NAME)
                .get(annotation.getName());
        List<Class<?>> annotationClasses = ReflectionUtils.forNames(annotationNames, ClassLoader.getSystemClassLoader());

        return new HashSet<>(annotationClasses);
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
