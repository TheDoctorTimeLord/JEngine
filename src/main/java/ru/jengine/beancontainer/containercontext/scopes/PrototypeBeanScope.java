package ru.jengine.beancontainer.containercontext.scopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;

public class PrototypeBeanScope extends AbstractBeanScope {
    private static final Logger LOG = LoggerFactory.getLogger(SingletonBeanScope.class);

    private final List<BeanProcessor> beanProcessors;
    private final Map<Class<?>, BeanDefinition> beanDefinitions;

    public PrototypeBeanScope(List<BeanDefinition> beanDefinitions, BeanFactory beanFactory,
            List<BeanProcessor> beanProcessors)
    {
        super(beanFactory);

        this.beanDefinitions = beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinition::getBeanClass, Function.identity()));

        this.beanProcessors = beanProcessors;
    }

    @Override
    public void prepareStart() {
        for (BeanProcessor beanProcessor : beanProcessors) {
            for (BeanDefinition definition : beanDefinitions.values()) {
                runPreConstruct(beanProcessor, definition, LOG);
            }
        }
    }

    @Override
    public void postProcess() { }

    @Override
    public void afterInitialize() { }

    @Override
    public void prepareStop() { }

    @Override
    public List<Class<?>> getBeanClasses() {
        return new ArrayList<>(beanDefinitions.keySet());
    }

    @Override
    public ResolvedBeanData getBean(ResolvingProperties properties) {
        BeanDefinition definition = beanDefinitions.get(properties.getRequestedClass());
        if (definition == null) {
            return ResolvedBeanData.NOT_RESOLVED;
        }

        ResolvedBeanData createdBean = constructBean(beanProcessors, definition, LOG);

        for (BeanProcessor beanProcessor : beanProcessors) {
            runPostConstruct(beanProcessor, createdBean, LOG);
        }

        return createdBean;
    }
}
