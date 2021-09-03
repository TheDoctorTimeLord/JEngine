package ru.jengine.beancontainer.implementation.factories;

import java.util.List;

import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.utils.AutowireUtils;
import ru.jengine.beancontainer.utils.BeanUtils;

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
                return BeanUtils.findAppropriateValueBean(multiContext, resultType, usedContexts, isCollection);
            });
        }

        return super.autowire(methodParameter);
    }
}
