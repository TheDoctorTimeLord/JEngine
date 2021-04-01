package ru.jengine.beancontainer.implementation.factories;

import ru.jengine.beancontainer.BeanPostProcessor;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.utils.BeanUtils;

import java.util.List;

public class AutowireConfigurableBeanFactory extends AutowireBeanFactory {
    private List<BeanPostProcessor> beanPostProcessors;

    public AutowireConfigurableBeanFactory(ContainerContext context) {
        super(context);
    }

    public void configure(ContainerContext infrastructureContext) {
        beanPostProcessors = BeanUtils.getBeanAsList(infrastructureContext.getBean(BeanPostProcessor.class));
    }

    @Override
    public BeanContext buildBean(Class<?> beanClass) {
        BeanContext beanContext = super.buildBean(beanClass);

        configureBean(beanContext);

        return beanContext;
    }

    private void configureBean(BeanContext beanContext) {
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            postProcessor.postProcess(beanContext, getContext());
        }
    }
}
