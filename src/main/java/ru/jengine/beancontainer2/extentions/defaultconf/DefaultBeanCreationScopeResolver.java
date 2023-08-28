package ru.jengine.beancontainer2.extentions.defaultconf;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.BeanCreationScope;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.containercontext.scopes.PrototypeBeanScope;
import ru.jengine.beancontainer2.containercontext.scopes.SingletonBeanScope;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer2.extentions.BeanProcessor;

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
