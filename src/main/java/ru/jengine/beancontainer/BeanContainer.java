package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;

public interface BeanContainer extends ContextPatternsHandler {
    void initializeCommonContexts(ContainerConfiguration configuration);
    <T> T getBean(Class<?> beanClass);
    void stop();
}
