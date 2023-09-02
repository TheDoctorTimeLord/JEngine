package ru.jengine.beancontainer2.beandefinitionreades;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beandefinitions.JavaClassBeanDefinition;

import java.util.List;
import java.util.stream.Collectors;

public class ExistingBeanDefinitionReader implements BeanDefinitionReader {
    private final List<Object> beans;

    public ExistingBeanDefinitionReader(List<Object> beans) {
        this.beans = beans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return beans.stream()
                .map(bean -> new JavaClassBeanDefinition(
                        bean.getClass(),
                        Constants.BeanScope.SINGLETON,
                        () -> bean
                ))
                .collect(Collectors.toList());
    }
}
