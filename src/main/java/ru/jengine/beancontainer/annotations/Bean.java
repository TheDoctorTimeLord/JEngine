package ru.jengine.beancontainer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.service.Constants;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    boolean isInfrastructure() default false;
    String strategyCode() default Constants.BeanStrategy.SINGLETON;
}
