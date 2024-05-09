package ru.jengine.jsonconverter.modules;

import ru.jengine.beancontainer.annotations.ImportModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableJsonConverter
@ImportModule(JsonConverterWithStandardToolsModule.class)
public @interface EnableJsonConverterWithStandardTools {
}
