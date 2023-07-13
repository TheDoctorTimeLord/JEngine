package ru.jengine.beancontainer.beandefinitionreades;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}
