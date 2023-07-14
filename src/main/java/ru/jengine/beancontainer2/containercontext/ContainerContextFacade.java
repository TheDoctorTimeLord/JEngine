package ru.jengine.beancontainer2.containercontext;

import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.utils.ReflectionContainerUtils;
import ru.jengine.utils.CollectionUtils;
import ru.jengine.utils.serviceclasses.Stoppable;

import javax.annotation.Nullable;
import java.util.*;

public class ContainerContextFacade implements BeanExtractor, Stoppable {
    private final Map<String, ContainerContext> containedContexts = new HashMap<>();

    public void registerContext(String contextName, ContainerContext context) {
        if (containedContexts.containsKey(contextName)) {
            throw new ContainerException("Context with name [%s] has been registered already".formatted(contextName));
        }

        containedContexts.put(contextName, context);
    }

    @Override
    @Nullable
    public Object getBean(ResolvingProperties properties) {
        boolean asCollection = validateCollectionClass(properties.getCollectionClass()) != null;
        List<Object> resolvedBeans = asCollection ? new ArrayList<>() : null;

        try {
            for (ContainerContext beanContextSource : getBeanSources(properties.getBeanContextSources())) {
                Object resolvedBean = beanContextSource.getBean(properties);
                if (resolvedBean != NOT_RESOLVED) {
                    if (asCollection) {
                        if (resolvedBean instanceof Collection<?> collectionResolvedBean
                                && !properties.getRequestedClass().isAssignableFrom(collectionResolvedBean.getClass()))
                        {
                            resolvedBeans.addAll(collectionResolvedBean);
                        } else {
                            resolvedBeans.add(resolvedBean);
                        }
                    } else {
                        return resolvedBean;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new ContainerException("Exception during creation bean [%s]".formatted(properties.getRequestedClass()), e);
        }

        return asCollection
                ? CollectionUtils.convertToCollection(resolvedBeans, properties.getCollectionClass())
                : NOT_RESOLVED;
    }

    private static Class<?> validateCollectionClass(Class<?> collectionClass) {
        if (!ReflectionContainerUtils.isAvailableCollection(collectionClass)) {
            throw new ContainerException("Unexpected collection class: " + collectionClass);
        }

        return collectionClass;
    }

    private Collection<ContainerContext> getBeanSources(List<String> beanContextSources) {
        if (beanContextSources == null) {
            return containedContexts.values();
        }

        List<ContainerContext> beanSources = new ArrayList<>(beanContextSources.size());
        for (String beanContextSource : beanContextSources) {
            ContainerContext containerContext = containedContexts.get(beanContextSource);
            if (containerContext == null) {
                throw new ContainerException("Context [%s] is not found".formatted(beanContextSource));
            }
            beanSources.add(containerContext);
        }
        return beanSources;
    }

    @Override
    public void stop() {
        Set<ContainerContext> stoppedContexts = new HashSet<>();
        for (ContainerContext context : containedContexts.values()) {
            if (!stoppedContexts.contains(context)) {
                context.stop();
                stoppedContexts.add(context);
            }
        }
    }
}
