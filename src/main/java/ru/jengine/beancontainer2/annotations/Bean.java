package ru.jengine.beancontainer2.annotations;

import ru.jengine.beancontainer.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    boolean isInfrastructure() default false;
    String strategyCode() default Constants.BeanStrategy.SINGLETON;
}
