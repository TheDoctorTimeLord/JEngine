package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanData;

public class BeanProcessorAdapter implements BeanProcessor
{
    @Override
    public void preConstructProcess(BeanDefinition beanDefinition) { }

    @Override
    public BeanData constructProcess(BeanData bean) {
        return bean;
    }

    @Override
    public void postConstructProcess(BeanData bean, BeanFactory beanFactory) { }

    @Override
    public void afterInitialize(BeanData bean) { }
}
