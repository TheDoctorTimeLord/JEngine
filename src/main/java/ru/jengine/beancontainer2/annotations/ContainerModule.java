package ru.jengine.beancontainer2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerModule {
    String contextName();
    String[] beanSources() default {};
    boolean needLoadOnContextInitialize() default true;
}
