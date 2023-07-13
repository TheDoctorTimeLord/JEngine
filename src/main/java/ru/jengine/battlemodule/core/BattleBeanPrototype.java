package ru.jengine.battlemodule.core;

import ru.jengine.beancontainer.Constants.BeanScope;
import ru.jengine.beancontainer.annotations.Bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для разметки бинов внутри боевой системы. Аннотация обеспечивает создание новых объектов на каждый вызов
 * {@link ru.jengine.beancontainer.JEngineContainer#getBean(Class)}, из-за этого требуется внимательно следить за
 * внедрением зависимостей. NOTE: {@link BeanScope#SINGLETON} подойдёт только для тех объектов, которые не хранят
 * локального состояния в бою.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Bean(scopeName = BeanScope.PROTOTYPE)
public @interface BattleBeanPrototype {
}
