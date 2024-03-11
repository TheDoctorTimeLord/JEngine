package ru.jengine.beancontainer.intstructure.pac13;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.ClassesWith;

import java.util.List;

@Bean
public class X {
    private final List<Class<?>> annotatedClasses;

    public X(@ClassesWith(TestAnnotation.class) List<Class<?>> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public List<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }
}
