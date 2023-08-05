package ru.jengine.beancontainer2.utils;

import ru.jengine.beancontainer2.containercontext.BeanExtractor;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.exceptions.ContainerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.jengine.beancontainer2.containercontext.BeanExtractor.NOT_RESOLVED;

public class BeanUtils { //TODO вынести в отдельный объект
    public static Object resolveBeansAsCollection(Collection<? extends BeanExtractor> extractors,
            ResolvingProperties[] properties, Class<?> collectionClass)
    {
        if (!ReflectionContainerUtils.isAvailableCollection(collectionClass)) {
            throw new ContainerException("Collection [%s] is not available. Possible: %s"
                    .formatted(collectionClass, ReflectionContainerUtils.AVAILABLE_COLLECTIONS));
        }

        List<Object> resolvedBeans = BeanUtils.resolveBeans(extractors, properties);
        return ReflectionContainerUtils.convertToCollection(resolvedBeans, collectionClass);
    }

    public static Object resolveBeansMayBeCollection(Collection<? extends BeanExtractor> extractors,
            ResolvingProperties properties)
    {
        Class<?> collectionClass = properties.getCollectionClass();
        if (collectionClass != null) {
            return resolveBeansAsCollection(extractors, new ResolvingProperties[] { properties }, collectionClass);
        }

        List<Object> resolvedBeans = BeanUtils.resolveBeans(extractors, properties);

        if (resolvedBeans.size() > 1) {
            throw new ContainerException("Too many candidates for [%s]. Candidates: %s"
                    .formatted(properties.getRequestedClass(), resolvedBeans));
        }

        return resolvedBeans.isEmpty() ? null : resolvedBeans.get(0);
    }

    public static List<Object> resolveBeans(Collection<? extends BeanExtractor> extractors,
            ResolvingProperties... propertiesForResolve) {
        Class<?> currentResolvedBeanClass = null;
        try {
            List<Object> resolvedBeans = new ArrayList<>();

            for (BeanExtractor beanContextSource : extractors) {
                for (ResolvingProperties properties : propertiesForResolve) {
                    currentResolvedBeanClass = properties.getRequestedClass();
                    Object resolvedBean = beanContextSource.getBean(properties);
                    if (resolvedBean != NOT_RESOLVED) {
                        if (resolvedBean instanceof Collection<?> collection
                                && !properties.getRequestedClass().isAssignableFrom(collection.getClass()))
                        {
                            resolvedBeans.addAll(collection);
                        } else {
                            resolvedBeans.add(resolvedBean);
                        }
                    }
                }
            }

            return resolvedBeans;
        }
        catch (Exception e) {
            throw new ContainerException("Exception during creation bean [%s]".formatted(currentResolvedBeanClass), e);
        }
    }
}
