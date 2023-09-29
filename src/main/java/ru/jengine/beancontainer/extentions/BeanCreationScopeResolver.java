package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanCreationScope;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;

import java.util.List;

public interface BeanCreationScopeResolver {
    BeanCreationScope resolve(String scopeName, List<BeanDefinition> definitions, BeanFactory factory,
            ContainerContext context, List<BeanProcessor> beanProcessors,
            List<BeanPreRemoveProcessor> preRemoveProcessors);
}
