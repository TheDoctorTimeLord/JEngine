package ru.jengine.beancontainer.implementation.beandefinitionreaders;

import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.implementation.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.implementation.factories.beanstrategies.ExistBeanFactoryStrategy;

public class ExistBeanDefinitionsReader implements BeanDefinitionReader {
    private final List<Object> beans;

    public ExistBeanDefinitionsReader(List<Object> beans) {
        this.beans = beans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return beans.stream()
                .map(bean -> new JavaClassBeanDefinition(bean.getClass(), new ExistBeanFactoryStrategy(bean)))
                .collect(Collectors.toList());
    }
}
