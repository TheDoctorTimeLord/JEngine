package ru.jengine.beancontainer.containercontext;

import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;

import java.util.ArrayList;
import java.util.List;

public class BeanResolver {
    public List<ResolvedBeanData> resolveBeans(BeanExtractor[] extractors,
            ResolvingProperties... propertiesForResolve) {
        Class<?> currentResolvedBeanClass = null;
        try {
            List<ResolvedBeanData> resolvedBeans = new ArrayList<>();

            for (ResolvingProperties properties : propertiesForResolve) {
                for (BeanExtractor beanExtractor : extractors) {
                    currentResolvedBeanClass = properties.getRequestedClass();

                    ResolvedBeanData resolvedBean = beanExtractor.getBean(properties);
                    if (resolvedBean.isResolved()) {
                        if (resolvedBean.isMultipleBean()) {
                            resolvedBeans.addAll(resolvedBean.asMultipleBeans());
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
