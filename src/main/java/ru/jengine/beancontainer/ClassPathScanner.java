package ru.jengine.beancontainer;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.exceptions.ContainerException;

import java.lang.annotation.Annotation;
import java.util.*;

public class ClassPathScanner implements ClassFinder {
    private final Map<Class<? extends Annotation>, Set<Class<?>>> annotatedClasses = new HashMap<>();
    private final Set<Class<?>> notIndexedClasses = new HashSet<>();
    private final Set<Class<?>> notIndexedAnnotations = new HashSet<>();

    {
        annotatedClasses.put(Bean.class, new HashSet<>());
    }

    @Override
    public void scan(String packageToScan) {
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<?>> scannedClasses = reflections.getTypesAnnotatedWith(Bean.class);
        for (Class<?> cls : scannedClasses) {
            if (cls.isAnnotation()) {
                notIndexedAnnotations.add(cls);
            } else {
                notIndexedClasses.add(cls);
            }
        }
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        index();
        return annotatedClasses.get(annotation);
    }

    private void index() {
        if (!notIndexedAnnotations.isEmpty()) {
            indexAnnotations();
        }
        if (!notIndexedClasses.isEmpty()) {
            indexClasses();
        }
    }

    private void indexAnnotations() {
        Map<Class<? extends Annotation>, Set<Class<?>>> index = new HashMap<>();
        for (Class<?> annotation : notIndexedAnnotations) {
            if (annotatedClasses.containsKey(annotation)) {
                throw new ContainerException("Annotation '%s' was find twice");
            }
            index.put((Class<? extends Annotation>) annotation, new HashSet<>());
        }
        notIndexedAnnotations.clear();

        annotatedClasses.values().stream()
                .flatMap(Collection::stream)
                .forEach(cls -> indexClassIfPresentAnnotations(cls, index));
        annotatedClasses.putAll(index);
    }

    private void indexClasses() {
        notIndexedClasses.forEach(cls -> indexClassIfPresentAnnotations(cls, annotatedClasses));
    }

    private static void indexClassIfPresentAnnotations(Class<?> cls, Map<Class<? extends Annotation>, Set<Class<?>>> annotated) {
        Set<Annotation> annotations = ReflectionUtils.getAnnotations(cls,
                annotation -> annotated.containsKey(annotation.annotationType()));
        if (annotations.isEmpty()) {
            return;
        }

        for (Annotation annotation : annotations) {
            annotated.get(annotation.annotationType()).add(cls);
        }
    }
}
