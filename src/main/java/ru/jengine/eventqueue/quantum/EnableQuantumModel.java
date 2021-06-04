package ru.jengine.eventqueue.quantum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.annotations.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({QuantumEventInterceptor.class, QuantaNotificationEventPoolHandler.class})
public @interface EnableQuantumModel {
}
