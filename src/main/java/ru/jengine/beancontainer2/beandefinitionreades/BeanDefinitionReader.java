package ru.jengine.beancontainer2.beandefinitionreades;

import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}
