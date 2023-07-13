package ru.jengine.beancontainer2.annotations;

import ru.jengine.beancontainer.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportModule {
    Class<? extends Module>[] value();
}
