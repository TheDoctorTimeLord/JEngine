package ru.test.annotation;

import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.ClassPathScanner;
import ru.jengine.beancontainer.annotations.ComponentScan;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.implementation.AnnotationModule;
import ru.jengine.beancontainer.implementation.BeanContainerImpl;

@ContainerModule
@ComponentScan("ru.test.annotation")
public class TestModule extends AnnotationModule {
    public static void main(String[] arg) {
        ClassFinder classFinder = new ClassPathScanner();
        BeanContainerImpl beanContainer = new BeanContainerImpl();
        beanContainer.initialize(TestModule.class, classFinder);
    }
}
