package ru.jengine.battlemodule.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;

/**
 * Аннотация для разметки бинов внутри боевой системы. Аннотация обеспечивает создание новых объектов на каждый вызов
 * {@link ru.jengine.beancontainer.BeanContainer#getBean(Class)}, из-за этого требуется внимательно следить за
 * внедрением зависимостей. NOTE: {@link BeanStrategy#SINGLETON} подойдёт только для тех объектов, которые не хранят
 * локального состояния в бою.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public @interface BattleBeanPrototype {
}
