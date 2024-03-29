package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanData;

public interface BeanProcessor {
    void preConstructProcess(BeanDefinition beanDefinition);
    BeanData constructProcess(BeanData bean);
    void postConstructProcess(BeanData bean, BeanFactory beanFactory);
    void afterInitialize(BeanData bean);
}
