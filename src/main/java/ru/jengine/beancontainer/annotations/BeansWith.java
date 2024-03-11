package ru.jengine.beancontainer.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeansWith {
    Class<? extends Annotation> value();
}
