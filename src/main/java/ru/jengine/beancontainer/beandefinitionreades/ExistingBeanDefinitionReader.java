package ru.jengine.beancontainer.beandefinitionreades;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition.BeanProducer;
import ru.jengine.beancontainer.beandefinitions.JavaClassBeanDefinition;

import java.util.List;

public class ExistingBeanDefinitionReader implements BeanDefinitionReader {
    private final List<Object> beans;

    public ExistingBeanDefinitionReader(List<Object> beans) {
        this.beans = beans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return beans.stream()
                .map(bean -> (BeanDefinition) new JavaClassBeanDefinition(
                        bean.getClass(),
                        Constants.BeanScope.SINGLETON,
                        new BeanProducer(() -> bean)
                ))
                .toList();
    }
}
