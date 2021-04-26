package ru.test.annotation;

import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.PackageScan;
import ru.jengine.beancontainer.implementation.BeanContainerImpl;
import ru.jengine.beancontainer.implementation.moduleimpls.AnnotationModule;

@ContainerModule
@PackageScan("ru.test.annotation")
public class TestModule extends AnnotationModule {
    public static void main(String[] arg) {
        BeanContainerImpl beanContainer = new BeanContainerImpl();
        beanContainer.initialize(TestModule.class);
    }
}
