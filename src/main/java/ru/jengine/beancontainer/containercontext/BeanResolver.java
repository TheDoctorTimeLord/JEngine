package ru.jengine.beancontainer.containercontext;

import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.jengine.beancontainer.containercontext.BeanExtractor.NOT_RESOLVED;

public class BeanResolver {
    //TODO Получать процессоры разрешения бинов (такие как Primal или бины, которые перезатирают другие)

    public Object resolveBeansAsCollection(BeanExtractor[] extractors,
            ResolvingProperties[] properties, Class<?> collectionClass)
    {
        if (!ReflectionContainerUtils.isAvailableCollection(collectionClass)) {
            throw new ContainerException("Collection [%s] is not available. Possible: %s"
                    .formatted(collectionClass, ReflectionContainerUtils.AVAILABLE_COLLECTIONS));
        }

        List<Object> resolvedBeans = resolveBeans(extractors, properties);
        return ReflectionContainerUtils.convertToCollection(resolvedBeans, collectionClass);
    }

    public Object resolveBeansMayBeCollection(BeanExtractor[] extractors,
            ResolvingProperties properties)
    {
        Class<?> collectionClass = properties.getCollectionClass();
        if (collectionClass != null) {
            return resolveBeansAsCollection(extractors, new ResolvingProperties[] { properties }, collectionClass);
        }

        List<Object> resolvedBeans = resolveBeans(extractors, properties);

        if (resolvedBeans.size() > 1) {
            throw new ContainerException("Too many candidates for [%s]. Candidates: %s"
                    .formatted(properties.getRequestedClass(), resolvedBeans));
        }

        return resolvedBeans.isEmpty() ? NOT_RESOLVED : resolvedBeans.get(0);
    }

    private List<Object> resolveBeans(BeanExtractor[] extractors,
            ResolvingProperties... propertiesForResolve) {
        Class<?> currentResolvedBeanClass = null;
        try {
            List<Object> resolvedBeans = new ArrayList<>();

            for (ResolvingProperties properties : propertiesForResolve) {
                for (BeanExtractor beanExtractor : extractors) {
                    currentResolvedBeanClass = properties.getRequestedClass();
                    Object resolvedBean = beanExtractor.getBean(properties);
                    if (BeanExtractor.isResolved(resolvedBean)) {
                        if (resolvedBean instanceof Collection<?> collection) {
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
