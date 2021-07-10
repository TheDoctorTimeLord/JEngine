package ru.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.JEngineContainer;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.eventqueue.Dispatcher;
import ru.jengine.eventqueue.quantum.EnableQuantumModel;
import ru.jengine.eventqueue.quantum.QuantumEvent;
import ru.test.annotation.quantum.SpecialAsyncMessageEvent;

@ContainerModule
@EnableQuantumModel
@PackageScan("ru.test.annotation")
public class TestModule extends AnnotationModule {
    public static void main(String[] arg) throws InterruptedException {
        JEngineContainer beanContainer = new JEngineContainer();
        beanContainer.initialize(TestModule.class);

        Dispatcher dispatcher = beanContainer.getBean(Dispatcher.class);

        dispatcher.registerEvent(new AsyncEventMessage("message 1"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 1"));
        dispatcher.registerEvent(new AsyncEventMessage("message 2"));
        dispatcher.registerEvent(new AsyncEventMessage("message 3"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 2"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 3"));
        dispatcher.registerEvent(new QuantumEvent());
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 4"));
        dispatcher.registerEvent(new AsyncEventMessage("message 4"));
        dispatcher.registerEvent(new AsyncEventMessage("message 5"));
        dispatcher.registerEvent(new QuantumEvent());
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 5"));

        Thread.sleep(6);
        beanContainer.stop();
    }
}

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface Anno1 {}

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Anno1
@interface Anno2 {}

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Anno2
@Bean
@interface Anno3 {}

@Anno3
class Sas {}
