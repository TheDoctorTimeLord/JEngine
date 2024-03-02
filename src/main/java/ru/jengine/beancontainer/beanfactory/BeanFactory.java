package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;

public interface BeanFactory {
    Object buildBean(BeanDefinition definition);

    Object[] findArguments(ParametersContainer parametersContainer);
}
