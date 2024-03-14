package ru.jengine.beancontainer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.utils.ReflectionUtils;

import javax.annotation.Nullable;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AnnotationUtils {
    private AnnotationUtils() {}

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationUtils.class);
    public static final List<Class<? extends Annotation>> systemAnnotations =
            List.of(Target.class, Retention.class, Override.class, Documented.class);

    private static final Map<Annotation, List<Annotation>> resolvedAnnotationCache = new ConcurrentHashMap<>();
    private static final Map<AnnotatedElement, List<Annotation>> allAnnotationByClassCache = new ConcurrentHashMap<>();

    public static List<Annotation> resolveNotSystemAnnotation(Annotation annotation) {
        List<Annotation> cached = resolvedAnnotationCache.get(annotation);
        if (cached != null) {
            return cached;
        }

        if (systemAnnotations.contains(annotation.annotationType())) {
            resolvedAnnotationCache.put(annotation, Collections.emptyList());
            return Collections.emptyList();
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

    public static List<Annotation> resolveNotSystemAnnotation(AnnotatedElement annotationOwner) {
        List<Annotation> cache = allAnnotationByClassCache.get(annotationOwner);
        if (cache != null) {
            return cache;
        }

        Stream<Annotation> declaredOnElement = Stream.of(annotationOwner.getAnnotations())
                .flatMap(annotation -> resolveNotSystemAnnotation(annotation).stream());

        Stream<Annotation> declaredOnSuperclass = annotationOwner instanceof Class<?> cls
                ? ReflectionUtils.walkBySuperclasses(cls.getSuperclass())
                        .flatMap(c -> Stream.of(c.getAnnotations()))
                        .filter(annotation -> annotation.annotationType().isAnnotationPresent(Inherited.class))
                        .flatMap(annotation -> resolveNotSystemAnnotation(annotation).stream())
                : Stream.of();

        List<Annotation> allAnnotations = Stream.concat(declaredOnElement, declaredOnSuperclass).toList();
        allAnnotationByClassCache.put(annotationOwner, allAnnotations);
        return allAnnotations;
    }

    public static List<Annotation> resolveNotSystemAnnotation(String annotationName) {
        try {
            return resolveNotSystemAnnotation(Class.forName(annotationName));
        }
        catch (ClassNotFoundException e) {
            LOG.error("Annotation [%s] is not found".formatted(annotationName), e);
            return new ArrayList<>();
        }
    }

    @Nullable
    public static <T extends Annotation> T getAnnotationSafe(AnnotatedElement owner, Class<T> annotation) {
        List<Annotation> allAnnotations = resolveNotSystemAnnotation(owner);
        for (Annotation innerAnnotation : allAnnotations) {
            if (innerAnnotation.annotationType().equals(annotation)) {
                return (T) innerAnnotation;
            }
        }
        return null;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement owner, Class<T> annotation) {
        T resultAnnotation = getAnnotationSafe(owner, annotation);

        if (resultAnnotation == null) {
            throw new ContainerException("Class [" + owner + "] has not annotation [" + annotation + "]");
        }

        return resultAnnotation;
    }

    public static <T extends Annotation> List<T> getAnnotations(AnnotatedElement owner, Class<T> annotation) {
        List<T> result = new ArrayList<>();

        List<Annotation> allAnnotations = resolveNotSystemAnnotation(owner);
        for (Annotation innerAnnotation : allAnnotations) {
            if (innerAnnotation.annotationType().equals(annotation)) {
                result.add((T) innerAnnotation);
            }
        }

        return result;
    }

    public static boolean isAnnotationPresent(AnnotatedElement owner, Class<?> annotation) {
        List<Annotation> allAnnotations = resolveNotSystemAnnotation(owner);
        for (Annotation innerAnnotation : allAnnotations) {
            if (innerAnnotation.annotationType().equals(annotation)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A extractAnnotation(Annotation[] annotations, Class<A> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.equals(annotation.annotationType())) {
                return (A) annotation;
            }
        }
        return null;
    }
}
