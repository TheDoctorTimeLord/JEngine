package ru.test.annotation;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.BeanContainerImpl;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;
import ru.jengine.eventqueue.Dispatcher;
import ru.test.annotation.quantum.QuantumEventImpl;
import ru.test.annotation.quantum.SpecialAsyncMessageEvent;

@ContainerModule
@PackageScan("ru.test.annotation")
public class TestModule extends AnnotationModule {
    public static void main(String[] arg) throws InterruptedException {
        BeanContainerImpl beanContainer = new BeanContainerImpl();
        beanContainer.initialize(TestModule.class);

        Dispatcher dispatcher = beanContainer.getBean(Dispatcher.class);

        dispatcher.registerEvent(new AsyncEventMessage("message 1"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 1"));
        dispatcher.registerEvent(new AsyncEventMessage("message 2"));
        dispatcher.registerEvent(new AsyncEventMessage("message 3"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 2"));
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 3"));
        dispatcher.registerEvent(new QuantumEventImpl());
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 4"));
        dispatcher.registerEvent(new AsyncEventMessage("message 4"));
        dispatcher.registerEvent(new AsyncEventMessage("message 5"));
        dispatcher.registerEvent(new QuantumEventImpl());
        dispatcher.registerEvent(new SpecialAsyncMessageEvent("message 5"));

        Thread.sleep(4);
        dispatcher.stopDispatcher();
    }
}
