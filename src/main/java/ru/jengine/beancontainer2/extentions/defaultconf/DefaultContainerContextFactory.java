package ru.jengine.beancontainer2.extentions.defaultconf;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
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
    @Override
    @SuppressWarnings("unchecked")
    public ContainerContext build(ContextMetainfo metainfo, BeanFactory beanFactory, ContainerState containerState) {
        BeanCreationScopeResolver beanCreationScopeResolver =
                containerState.getContainerConfiguration().getBeanCreationScopeResolver();
        ContainerContextFacade containerContextFacade = containerState.getContainerContextFacade();

        List<BeanProcessor> beanProcessors = (List<BeanProcessor>)containerContextFacade
                .getBean(ResolvingProperties
                        .properties(BeanProcessor.class)
                        .collectionClass(List.class));
        List<BeanPreRemoveProcessor> beanPreRemoveProcessors = (List<BeanPreRemoveProcessor>)containerContextFacade
                .getBean(ResolvingProperties
                        .properties(BeanProcessor.class)
                        .collectionClass(List.class));

        List<BeanDefinition> beanDefinitions = metainfo.extractBeanDefinitions();

        return new ScopableContainerContext(beanFactory, beanDefinitions, beanCreationScopeResolver, beanProcessors,
                beanPreRemoveProcessors);
    }
}
