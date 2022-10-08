package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;

public interface BeanContainer extends ContextPatternsHandler {
    void initializeCommonContexts(ContainerConfiguration configuration);
    <T, R> R getBean(Class<? extends T> beanClass);
    <T> T getBean(String contextName, Class<? extends T> beanClass);
    void stop();
}
