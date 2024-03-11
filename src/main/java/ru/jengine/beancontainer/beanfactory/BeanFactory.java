package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.utils.ParametersContainer;

public interface BeanFactory {
    Object buildBean(BeanDefinition definition);

    Object[] findArguments(ParametersContainer parametersContainer);
}
