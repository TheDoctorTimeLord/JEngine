package ru.jengine.beancontainer.infrastructuretools;

import org.reflections.ReflectionUtils;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingPropertyDefinition;
import ru.jengine.beancontainer.events.FinishingInitializeContextsPhase;
import ru.jengine.beancontainer.events.LoadedContextMetainfoEvent;
import ru.jengine.beancontainer.events.RemoveContextEvent;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.extentions.infrastrucure.ContainerStateProvidable;
import ru.jengine.beancontainer.statepublisher.ContainerEventDispatcher;
import ru.jengine.beancontainer.statepublisher.ContainerListener;
import ru.jengine.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.MethodMeta;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

@Bean(isInfrastructure = true)
public class SharedBeansManager implements ContainerStateProvidable {
    private final Set<String> contextsWithSharedBeans = new HashSet<>();
    private final Map<String, List<SharedBeansProvidersContext>> sharedProviders = new HashMap<>();
    private BeanExtractor sharedBeansExtractor;
    private boolean afterContainerInitialized = false;

    @Override
    public void provide(ContainerState containerState) {
        this.sharedBeansExtractor = containerState.getContainerContextFacade();

        ContainerEventDispatcher dispatcher = containerState.getContainerEventDispatcher();
        dispatcher.registerListener(new SharedInfoRegister());
        dispatcher.registerListener(new ContextRemovingHandler());
        dispatcher.registerListener(new AfterInitializeHandler());
    }

    private void reshareBeans() {
        String[] beanSources = contextsWithSharedBeans.toArray(String[]::new);

        for (Map.Entry<String, List<SharedBeansProvidersContext>> entry : sharedProviders.entrySet()) {
            for (SharedBeansProvidersContext providersContext : entry.getValue()) {
                for (SharedBeansProvider provider : providersContext.providers()) {
                    if (!provider.isInitialized()) {
                        initializeProvider(entry.getKey(), provider, providersContext);
                    }

                    Object[] arguments = Arrays.stream(provider.argumentResolvingProperties)
                            .map(properties -> {
                                ResolvedBeanData resolveResult = sharedBeansExtractor.getBean(properties.beanContextSource(beanSources));
                                return resolveResult.isResolved()
                                        ? resolveResult.getBeanValue()
                                        : ReflectionContainerUtils.convertToCollection(null, properties.getCollectionClass());
                            })
                            .toArray(Object[]::new);
                    provider.provider.invoke(arguments);
                }
            }
        }
    }

    private void initializeProvider(String ownerContextName, SharedBeansProvider provider,
            SharedBeansProvidersContext providersContext)
    {
        ResolvedBeanData resolveResult = sharedBeansExtractor.getBean(ResolvingProperties
                .properties(providersContext.providersOwnerClass())
                .beanContextSource(ownerContextName));

        if (resolveResult.isResolved()) {
            provider.initializeProvider(resolveResult.getBeanValue());
        } else {
            throw new ContainerException("Can not resolve bean [%s] which contains shared bean provider".formatted(providersContext.providersOwnerClass()));
        }
    }

    private class SharedInfoRegister implements ContainerListener<LoadedContextMetainfoEvent> {
        @Override
        public Class<LoadedContextMetainfoEvent> getListenedEventClass() {
            return LoadedContextMetainfoEvent.class;
        }

        @Override
        public void handle(LoadedContextMetainfoEvent event, ContainerState containerState) {
            String contextName = event.contextName();

            for (BeanDefinition bean : event.metainfo().extractBeanDefinitions()) {
                if (bean.isShared()) {
                    contextsWithSharedBeans.add(contextName);
                }

                Class<?> beanClass = bean.getBeanClass();

                Set<Method> methods = ReflectionUtils.getAllMethods(beanClass,
                        method -> AnnotationUtils.isAnnotationPresent(method, ru.jengine.beancontainer.annotations.SharedBeansProvider.class));
                if (methods.isEmpty()) {
                    continue;
                }

                SharedBeansProvider[] providers = methods.stream()
                        .map(method -> {
                            ResolvingPropertyDefinition[] argumentsProperties = convertArgumentsToProperties(method);
                            return new SharedBeansProvider(method, argumentsProperties);
                        })
                        .toArray(SharedBeansManager.SharedBeansProvider[]::new);
                SharedBeansManager.this.sharedProviders.computeIfAbsent(contextName, k -> new ArrayList<>())
                        .add(new SharedBeansProvidersContext(beanClass, providers));
            }

            if (afterContainerInitialized) {
                reshareBeans();
            }
        }

        private static ResolvingPropertyDefinition[] convertArgumentsToProperties(Method method) {
            Type[] argumentTypes = method.getGenericParameterTypes();
            Annotation[][] argumentsAnnotations = method.getParameterAnnotations();
            ResolvingPropertyDefinition[] resolvingProperties = new ResolvingPropertyDefinition[argumentTypes.length];

            for (int i = 0; i < argumentTypes.length; i++) {
                try {
                    Type argumentType = argumentTypes[i];
                    Class<?> collectionGenericType = ReflectionContainerUtils.getCollectionGenericType(argumentType);
                    resolvingProperties[i] = ResolvingProperties.properties(collectionGenericType)
                            .collectionClass(ReflectionContainerUtils.castToClass(argumentType))
                            .annotated(argumentsAnnotations[i])
                            .expectedAnnotation(ru.jengine.beancontainer.annotations.Shared.class);
                } catch (ContainerException e) {
                    throw new ContainerException("Shared beans provider must have collection of [%s] by %d argument in [%s]"
                            .formatted(ReflectionContainerUtils.getAvailableCollectionAsString(), i, method), e);
                }
            }

            return resolvingProperties;
        }
    }

    private class ContextRemovingHandler implements ContainerListener<RemoveContextEvent> {
        @Override
        public Class<RemoveContextEvent> getListenedEventClass() {
            return RemoveContextEvent.class;
        }

        @Override
        public void handle(RemoveContextEvent event, ContainerState containerState) {
            String removedContextName = event.contextName();

            sharedProviders.remove(removedContextName);
            if (contextsWithSharedBeans.remove(removedContextName) && afterContainerInitialized) {
                reshareBeans();
            }
        }
    }

    private class AfterInitializeHandler implements ContainerListener<FinishingInitializeContextsPhase> {
        @Override
        public Class<FinishingInitializeContextsPhase> getListenedEventClass() {
            return FinishingInitializeContextsPhase.class;
        }

        @Override
        public void handle(FinishingInitializeContextsPhase event, ContainerState containerState) {
            afterContainerInitialized = true;
            reshareBeans();
        }
    }

    private record SharedBeansProvidersContext(Class<?> providersOwnerClass, SharedBeansProvider[] providers) {}

    private static class SharedBeansProvider {
        private final ResolvingPropertyDefinition[] argumentResolvingProperties;
        private Method providerMethod;
        private MethodMeta provider;

        public SharedBeansProvider(Method providerMethod, ResolvingPropertyDefinition[] argumentResolvingProperties)
        {
            this.providerMethod = providerMethod;
            this.argumentResolvingProperties = argumentResolvingProperties;
        }

        private void initializeProvider(Object methodOwner) {
            this.provider = new MethodMeta(providerMethod, methodOwner);
            this.providerMethod = null;
        }

        private boolean isInitialized() {
            return this.provider != null;
        }
    }
}
