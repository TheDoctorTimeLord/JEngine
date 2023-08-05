package ru.jengine.beancontainer2.extentions;

import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.BeanCreationScope;

import java.util.List;

public interface BeanCreationScopeResolver {
    BeanCreationScope resolve(String scopeName, List<BeanDefinition> definitions, BeanFactory factory,
            List<BeanProcessor> beanProcessors, List<BeanPreRemoveProcessor> preRemoveProcessors);
}
