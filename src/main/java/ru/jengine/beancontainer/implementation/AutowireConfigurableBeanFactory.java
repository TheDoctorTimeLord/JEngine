package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.dataclasses.BeanContext;

public class AutowireConfigurableBeanFactory extends AutowireBeanFactory {
    public AutowireConfigurableBeanFactory(ContainerContext context) {
        super(context);
    }

    public void configure(ContainerContext infrastructureContext) {

    }

    @Override
    public BeanContext buildBean(Class<?> beanClass) {
        return super.buildBean(beanClass);
    }
}
