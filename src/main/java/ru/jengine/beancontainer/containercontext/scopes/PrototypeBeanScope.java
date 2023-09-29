package ru.jengine.beancontainer.containercontext.scopes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrototypeBeanScope extends AbstractBeanScope {
    private static final Logger LOG = LoggerFactory.getLogger(SingletonBeanScope.class);

    private final List<BeanProcessor> beanProcessors;
    private final Map<Class<?>, BeanDefinition> beanDefinitions;
    private final ContainerContext parent;

    public PrototypeBeanScope(List<BeanDefinition> beanDefinitions, BeanFactory beanFactory, ContainerContext parent,
            List<BeanProcessor> beanProcessors)
    {
        super(beanFactory);

        this.beanDefinitions = beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinition::getBeanClass, Function.identity()));

        this.beanProcessors = beanProcessors;
        this.parent = parent;
    }

    @Override
    public void prepareStart() {
        for (BeanProcessor beanProcessor : beanProcessors) {
            for (BeanDefinition definition : beanDefinitions.values()) {
                runPreConstruct(beanProcessor, definition, parent, LOG);
            }
        }
    }

    @Override
    public void postProcess() { }

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

        ResolvedBeanData createdBean = constructBean(beanProcessors, definition, parent, LOG);

        for (BeanProcessor beanProcessor : beanProcessors) {
            runPostConstruct(beanProcessor, createdBean, parent, LOG);
        }

        return createdBean;
    }
}
