package ru.jengine.beancontainer.extentions.defaultconf;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanCreationScope;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.scopes.PrototypeBeanScope;
import ru.jengine.beancontainer.containercontext.scopes.SingletonBeanScope;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.BeanProcessor;

import java.util.List;

public class DefaultBeanCreationScopeResolver implements BeanCreationScopeResolver {
    @Override
    public BeanCreationScope resolve(String scopeName, List<BeanDefinition> definitions, BeanFactory factory,
            ContainerContext context, List<BeanProcessor> beanProcessors, List<BeanPreRemoveProcessor> preRemoveProcessors)
    {
        if (Constants.BeanScope.SINGLETON.equals(scopeName)) {
            return new SingletonBeanScope(definitions, factory, context, beanProcessors, preRemoveProcessors);
        }
        if (Constants.BeanScope.PROTOTYPE.equals(scopeName)) {
            return new PrototypeBeanScope(definitions, factory, context, beanProcessors);
        }

        throw new ContainerException("Unknown scope name [%s]".formatted(scopeName));
    }
}
