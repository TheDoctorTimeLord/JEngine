package ru.jengine.beancontainer2.containercontext;

public interface BeanCreationScope {
    Object getBean(ResolvingProperties properties);
}
