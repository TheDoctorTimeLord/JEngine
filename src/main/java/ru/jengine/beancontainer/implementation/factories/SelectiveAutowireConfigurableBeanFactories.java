package ru.jengine.beancontainer.implementation.factories;

import java.util.List;

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
            return AutowireUtils.autowire(methodParameter, context, (resultType, ctx) -> {
                ContainerMultiContext multiContext = (ContainerMultiContext) ctx;

                for (String contextName : usedContexts) {
                    BeanContext beanContext = multiContext.getBean(contextName, resultType);
                    if (beanContext != null) {
                        return beanContext;
                    }
                }
                return null;
            });
        }

        return super.autowire(methodParameter);
    }
}
