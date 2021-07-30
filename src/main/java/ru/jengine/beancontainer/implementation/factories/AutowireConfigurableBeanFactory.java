package ru.jengine.beancontainer.implementation.factories;

import java.util.List;

import ru.jengine.beancontainer.BeanPostProcessor;
import ru.jengine.beancontainer.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.ConfigurableBeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.utils.BeanUtils;

public class AutowireConfigurableBeanFactory extends AutowireBeanFactory implements ConfigurableBeanFactory {
    private List<BeanPostProcessor> beanPostProcessors;
    private List<BeanPreRemoveProcessor> beanPreRemoveProcessors;

    public AutowireConfigurableBeanFactory(ContainerContext context) {
        super(context);
    }

    @Override
    public void configure(ContainerContext infrastructureContext) {
        beanPostProcessors = BeanUtils.getBeanAsList(infrastructureContext.getBean(BeanPostProcessor.class));
        beanPreRemoveProcessors = BeanUtils.getBeanAsList(infrastructureContext.getBean(BeanPreRemoveProcessor.class));
    }

    @Override
    public BeanContext buildBean(Class<?> beanClass) {
        BeanContext beanContext = super.buildBean(beanClass);

        configureBean(beanContext);

        return beanContext;
    }

    @Override
    public void beforeRemove(BeanContext beanContext) {
        for (BeanPreRemoveProcessor preRemoveProcessor : beanPreRemoveProcessors) {
            preRemoveProcessor.preRemoveProcess(beanContext, getContext());
        }
    }

    private void configureBean(BeanContext beanContext) {
        for (BeanPostProcessor postProcessor : beanPostProcessors) {
            postProcessor.postProcess(beanContext, getContext());
        }
    }
}
