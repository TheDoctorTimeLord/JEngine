package ru.jengine.beancontainer2.containercontext.scopes;

import org.slf4j.Logger;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.BeanCreationScope;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer2.extentions.BeanProcessor;

import java.util.function.Supplier;

public abstract class AbstractBeanScope implements BeanCreationScope {
    private final BeanFactory beanFactory;

    protected AbstractBeanScope(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected void runPreConstruct(BeanProcessor beanProcessor, BeanDefinition definition, ContainerContext parent,
            Logger logger)
    {
        try {
            beanProcessor.preConstructProcess(definition, parent);
        }
        catch (Exception e) {
            logger.error("Exception during preprocessing [%s] by [%s]".formatted(definition.getBeanClass(), beanProcessor), e);
            throw e;
        }
    }

    protected Object runConstruct(BeanProcessor beanProcessor, Object createdBean, Class<?> beanClass,
            ContainerContext parent, Logger logger)
    {
        try {
            return beanProcessor.constructProcess(createdBean, beanClass, parent);
        }
        catch (Exception e) {
            logger.error("Exception during construct processing [%s] by [%s]".formatted(beanClass, beanProcessor), e);
            throw e;
        }
    }

    protected void runPostConstruct(BeanProcessor beanProcessor, Object createdBean, Class<?> beanClass,
            ContainerContext parent, Logger logger)
    {
        try {
            beanProcessor.postConstructProcess(createdBean, beanClass, parent);
        }
        catch (Exception e) {
            logger.error("Exception during postprocessing [%s] by [%s]".formatted(beanClass, beanProcessor), e);
            throw e;
        }
    }

    protected void runPreRemove(BeanPreRemoveProcessor preRemoveProcessor, Object createdBean, Class<?> beanClass,
            ContainerContext parent, Logger logger)
    {
        try {
            preRemoveProcessor.preRemoveProcess(createdBean, beanClass, parent);
        } catch (Exception e) {
            logger.error("Exception during pre remove [%s] by [%s]".formatted(beanClass, preRemoveProcessor), e);
        }
    }

    protected Object createBean(BeanDefinition definition, Logger logger) {
        Supplier<Object> beanProducer = definition.getBeanProducer();
        try {
            return beanProducer == null
                    ? beanFactory.buildBean(definition.getBeanClass())
                    : beanProducer.get();
        }
        catch (Exception e) {
            String beanProducerStr = beanProducer == null ? "beanFactory" : beanProducer.toString();
            logger.error("Exception during constructing [%s]. Producer [%s]".formatted(definition.getBeanClass(), beanProducerStr), e);
            throw e;
        }
    }
}
