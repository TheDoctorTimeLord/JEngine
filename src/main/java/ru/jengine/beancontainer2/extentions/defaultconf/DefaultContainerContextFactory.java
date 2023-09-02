package ru.jengine.beancontainer2.extentions.defaultconf;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.BeanResolver;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer2.containercontext.contexts.ScopableContainerContext;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer2.extentions.BeanProcessor;
import ru.jengine.beancontainer2.extentions.ContainerContextFactory;

import java.util.List;

public class DefaultContainerContextFactory implements ContainerContextFactory {
    private volatile BeanResolver beanResolver;

    @Override
    public ContainerContext build(String builtContextName, ContextMetainfo metainfo, BeanFactory beanFactory, ContainerState containerState) {
        BeanCreationScopeResolver beanCreationScopeResolver =
                containerState.getContainerConfiguration().getBeanCreationScopeResolver();
        ContainerContextFacade containerContextFacade = containerState.getContainerContextFacade();

        List<BeanProcessor> beanProcessors =
                getInfrastructureHandlers(builtContextName, containerContextFacade, BeanProcessor.class);
        List<BeanPreRemoveProcessor> beanPreRemoveProcessors =
                getInfrastructureHandlers(builtContextName, containerContextFacade, BeanPreRemoveProcessor.class);

        List<BeanDefinition> beanDefinitions = metainfo.extractBeanDefinitions();
        BeanResolver beanResolver = getBeanResolver(builtContextName);

        return new ScopableContainerContext(beanFactory, beanResolver, beanDefinitions, beanCreationScopeResolver,
                beanProcessors, beanPreRemoveProcessors);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getInfrastructureHandlers(String builtContextName,
            ContainerContextFacade containerContextFacade, Class<T> handlersClass)
    {
        if (Constants.Contexts.INFRASTRUCTURE_CONTEXT.equals(builtContextName)) {
            return List.of();
        }

        return (List<T>)containerContextFacade
                .getBean(ResolvingProperties
                        .properties(handlersClass)
                        .beanContextSource(Constants.Contexts.INFRASTRUCTURE_CONTEXT)
                        .collectionClass(List.class));
    }

    private BeanResolver getBeanResolver(String builtContextName) {
        if (Constants.Contexts.INFRASTRUCTURE_CONTEXT.equals(builtContextName)) {
            return new BeanResolver();
        }

        if (beanResolver == null) {
            synchronized (this) {
                if (beanResolver == null) {
                    beanResolver = new BeanResolver();
                }
            }
        }
        return beanResolver;
    }
}
