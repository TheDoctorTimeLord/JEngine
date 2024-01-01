package ru.jengine.beancontainer.extentions.defaultconf;

import java.util.List;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanResolver;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.containercontext.contexts.ScopableContainerContext;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.events.FinishingInitializeContextsPhase;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.ContainerContextFactory;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;
import ru.jengine.beancontainer.statepublisher.ContainerListener;

public class DefaultContainerContextFactory implements ContainerContextFactory {
    private static class AfterInitializeContainerListener implements ContainerListener<FinishingInitializeContextsPhase> {
        private final ContainerContext context;

        public AfterInitializeContainerListener(ContainerContext context) {
            this.context = context;
        }

        @Override
        public Class<FinishingInitializeContextsPhase> getListenedEventClass() {
            return FinishingInitializeContextsPhase.class;
        }

        @Override
        public void handle(FinishingInitializeContextsPhase event, ContainerState containerState) {
            context.afterInitialize();
            removeListener(containerState);
        }
    }

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
        BeanResolver resolver = getBeanResolver(builtContextName);
        ContainerContext context = new ScopableContainerContext(beanFactory, resolver, beanDefinitions,
                beanCreationScopeResolver, beanProcessors, beanPreRemoveProcessors);

        containerState.getContainerEventDispatcher().registerListener(new AfterInitializeContainerListener(context));

        return context;
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getInfrastructureHandlers(String builtContextName,
            ContainerContextFacade containerContextFacade, Class<T> handlersClass)
    {
        if (Constants.Contexts.INFRASTRUCTURE_CONTEXT.equals(builtContextName)) {
            return List.of();
        }

        return (List<T>)containerContextFacade
                .getBean(ResolvingProperties
                        .properties(handlersClass)
                        .beanContextSource(Constants.Contexts.INFRASTRUCTURE_CONTEXT)
                        .collectionClass(List.class))
                .getBeanValue();
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
