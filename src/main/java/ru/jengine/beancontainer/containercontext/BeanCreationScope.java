package ru.jengine.beancontainer.containercontext;

import java.util.List;

public interface BeanCreationScope extends BeanExtractor {
    void prepareStart();
    void postProcess();
    void afterInitialize();
    void prepareStop();

    List<Class<?>> getBeanClasses();
}
