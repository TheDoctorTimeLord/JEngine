package ru.jengine.beancontainer.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.exceptions.UtilsException;

import com.google.common.collect.ImmutableList;

public class AnnotationUtils {
    public static final List<Class<? extends Annotation>> systemAnnotations = ImmutableList.of(Target.class,
            Retention.class, Override.class);

    private final static Map<Annotation, List<Annotation>> resolvedAnnotationCache = new ConcurrentHashMap<>();
    private final static Map<Class<?>, List<Annotation>> allAnnotationByClassCache = new ConcurrentHashMap<>();

    public static List<Annotation> resolveNotSystemAnnotation(Annotation annotation) {
        List<Annotation> cached = resolvedAnnotationCache.get(annotation);
        if (cached != null) {
            return cached;
        }

        List<Annotation> result = new ArrayList<>();
        result.add(annotation);

        for (Annotation innerAnnotation : annotation.annotationType().getAnnotations()) {
            if (systemAnnotations.contains(innerAnnotation.annotationType())) {
                continue;
            }
            result.addAll(resolveNotSystemAnnotation(innerAnnotation));
        }

        resolvedAnnotationCache.put(annotation, result);
        return result;
    }

    public static List<Annotation> resolveNotSystemAnnotation(Class<?> annotationOwner) {
        List<Annotation> cache = allAnnotationByClassCache.get(annotationOwner);
        if (cache != null) {
            return cache;
        }

        List<Annotation> result = Stream.of(annotationOwner.getAnnotations())
                .map(AnnotationUtils::resolveNotSystemAnnotation)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        allAnnotationByClassCache.put(annotationOwner, result);

        return result;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> owner, Class<T> annotation) {
        List<Annotation> allAnnotations = resolveNotSystemAnnotation(owner);
        for (Annotation innerAnnotation : allAnnotations) {
            if (innerAnnotation.annotationType().equals(annotation)) {
                return (T) innerAnnotation;
            }
        }
        throw new UtilsException("Class [" + owner + "] has not annotation [" + annotation + "]");
    }

    public static <T extends Annotation> List<T> getAnnotations(Class<?> owner, Class<T> annotation) {
        List<T> result = new ArrayList<>();

        List<Annotation> allAnnotations = resolveNotSystemAnnotation(owner);
        for (Annotation innerAnnotation : allAnnotations) {
            if (innerAnnotation.annotationType().equals(annotation)) {
                result.add((T) innerAnnotation);
            }
        }

        return result;
    }
}
