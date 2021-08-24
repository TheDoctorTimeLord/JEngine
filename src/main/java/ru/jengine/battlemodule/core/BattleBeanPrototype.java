package ru.jengine.battlemodule.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.service.Constants.BeanStrategy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Bean(strategyCode = BeanStrategy.PROTOTYPE)
public @interface BattleBeanPrototype {
}
