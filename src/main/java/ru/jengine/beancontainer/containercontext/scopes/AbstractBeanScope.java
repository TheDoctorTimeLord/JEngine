package ru.jengine.beancontainer.containercontext.scopes;

import org.slf4j.Logger;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanCreationScope;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.BeanProcessor;

import java.util.List;
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

    protected ResolvedBeanData constructBean(List<BeanProcessor> beanProcessors, BeanDefinition beanDefinition,
            ContainerContext parent, Logger logger)
    {
        Object beanValue = createBean(beanDefinition, logger);
        ResolvedBeanData createdBean = new ResolvedBeanData(beanValue, beanDefinition.getBeanClass());

        for (BeanProcessor beanProcessor : beanProcessors) {
            try {
                beanProcessor.constructProcess(createdBean, parent);
            } catch (Exception e) {
                logger.error("Exception during construct processing [%s] by [%s]"
                        .formatted(beanDefinition.getBeanClass(), beanProcessor), e);
                throw e;
            }
        }

        return createdBean;
    }

    protected void runPostConstruct(BeanProcessor beanProcessor, BeanData createdBean, ContainerContext parent,
            Logger logger)
    {
        try {
            beanProcessor.postConstructProcess(createdBean, parent);
        }
        catch (Exception e) {
            logger.error("Exception during postprocessing [%s] by [%s]"
                    .formatted(createdBean.getBeanBaseClass(), beanProcessor), e);
            throw e;
        }
    }

    protected void runPreRemove(BeanPreRemoveProcessor preRemoveProcessor, BeanData createdBean,
            ContainerContext parent, Logger logger)
    {
        try {
            preRemoveProcessor.preRemoveProcess(createdBean, parent);
        } catch (Exception e) {
            logger.error("Exception during pre remove [%s] by [%s]"
                    .formatted(createdBean.getBeanBaseClass(), preRemoveProcessor), e);
        }
    }

    private Object createBean(BeanDefinition definition, Logger logger) {
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
