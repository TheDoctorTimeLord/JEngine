package ru.jengine.beancontainer.utils;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

public class AnnotationUtilsTest {
    @Test
    public void testInheritedAnnotations() {
        List<Class<? extends Annotation>> actual = AnnotationUtils.resolveNotSystemAnnotation(Start.class).stream()
                .map(Annotation::annotationType)
                .collect(Collectors.toList());

        MatcherAssert.assertThat(actual, hasItem(OnClass1.class));
        MatcherAssert.assertThat(actual, hasItem(OnClass2.class));
        MatcherAssert.assertThat(actual, hasItem(OnSuperClassPresented.class));
        MatcherAssert.assertThat(actual, hasItem(OnPresented1.class));
        MatcherAssert.assertThat(actual, not(hasItem(OnSuperClassNotPresented.class)));
        MatcherAssert.assertThat(actual, not(hasItem(OnPresented2.class)));
    }

    @Test
    public void testFunctionalInterfaceResolveWithoutExceptions() {
        AnnotationUtils.resolveNotSystemAnnotation("java.lang.FunctionalInterface");
    }

    @OnClass1
    @OnClass2
    private static class Start extends Superclass {}

    @OnSuperClassPresented
    @OnSuperClassNotPresented
    private static class Superclass {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnClass1 {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnClass2 {}

    @Inherited
    @OnPresented1
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnSuperClassPresented {}

    @OnPresented2
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnSuperClassNotPresented {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnPresented1 {}

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface OnPresented2 {}
}
