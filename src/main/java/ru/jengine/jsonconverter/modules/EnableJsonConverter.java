package ru.jengine.jsonconverter.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.annotations.ImportModule;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportModule({ JsonConverterModule.class })
public @interface EnableJsonConverter {
}
