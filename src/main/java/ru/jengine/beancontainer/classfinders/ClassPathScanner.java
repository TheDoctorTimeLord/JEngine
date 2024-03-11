package ru.jengine.beancontainer.classfinders;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import ru.jengine.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathScanner implements ClassFinder {
    private Reflections reflections;

    @Override
    public void scan(String scanningPackage) {
        reflections = new Reflections(ConfigurationBuilder
                .build(scanningPackage) //TODO исправить баг с чтением путей (читает по подстроке от последней папки)
                .setScanners(new TypeAnnotationsScannerExtensions(), new SubTypesScanner())
        );
    }

    @Override
    public Set<Class<?>> getAnnotatedTypes(Class<? extends Annotation> annotation) {
        Collection<String> annotationNames = reflections.getStore()
                .get(TypeAnnotationsScannerExtensions.NAME)
                .get(annotation.getName());
        List<Class<?>> annotationClasses = org.reflections.ReflectionUtils.forNames(annotationNames, ClassLoader.getSystemClassLoader());

        return new HashSet<>(annotationClasses);
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return getAnnotatedTypes(annotation).stream()
                .filter(ReflectionUtils.IS_CLASS_PREDICATE)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> Set<Class<? extends T>> getSubclasses(Class<T> cls) {
        return reflections.getSubTypesOf(cls);
    }
}
