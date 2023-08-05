package ru.jengine.beancontainer2.containercontext;

import java.util.List;

public interface BeanCreationScope extends BeanExtractor {
    void prepareStart();
    void postProcess();
    void prepareStop();

    List<Class<?>> getBeanClasses();
}
