package ru.jengine.beancontainer.implementation.factories;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.utils.AutowireUtils;

public class SelectiveAutowireConfigurableBeanFactories extends AutowireConfigurableBeanFactory {
    private final List<String> usedContexts;

    public SelectiveAutowireConfigurableBeanFactories(ContainerContext context, List<String> usedContexts) {
        super(context);
        this.usedContexts = usedContexts;
    }

    @Override
    protected Object autowire(MethodParameter methodParameter) {
        ContainerContext context = getContext();

        if (context instanceof ContainerMultiContext) {
            return AutowireUtils.autowire(methodParameter, context, (resultType, ctx, isCollection) -> {
                ContainerMultiContext multiContext = (ContainerMultiContext) ctx;

                return isCollection
                        ? findMultiValueBean(multiContext, resultType)
                        : findSingleValueBean(multiContext, resultType);
            });
        }

        return super.autowire(methodParameter);
    }

    private BeanContext findSingleValueBean(ContainerMultiContext multiContext, Class<?> resultType) {
        for (String contextName : usedContexts) {
            BeanContext beanContext =
                    checkNotEmptyCollection(multiContext.getBean(contextName, resultType));
            if (beanContext != null) {
                return beanContext;
            }
        }
        return null;
    }

    private BeanContext findMultiValueBean(ContainerMultiContext multiContext, Class<?> resultType) {
        return new BeanContext(
                usedContexts.stream()
                        .map(contextName -> multiContext.getBean(contextName, resultType))
                        .filter(Objects::nonNull)
                        .filter(beanContext -> beanContext.getBean() != null)
                        .flatMap(beanContext -> beanContext.isCollectionBean()
                                ? ((Collection<?>)beanContext.getBean()).stream()
                                : Stream.of((Object)beanContext.getBean()))
                        .collect(Collectors.toList()),
                resultType
        );
    }

    private static BeanContext checkNotEmptyCollection(BeanContext beanContext) {
        if (beanContext == null) {
            return null;
        }
        return  beanContext.getBean() instanceof Collection ? null : beanContext;
    }
}
