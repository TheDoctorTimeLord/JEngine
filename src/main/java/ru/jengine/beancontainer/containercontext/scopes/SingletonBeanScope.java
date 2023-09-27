package ru.jengine.beancontainer.containercontext.scopes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.BeanProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SingletonBeanScope extends AbstractBeanScope {
    private static final Logger LOG = LoggerFactory.getLogger(SingletonBeanScope.class);

    private final List<BeanProcessor> beanProcessors;
    private final List<BeanPreRemoveProcessor> preRemoveProcessors;
    private final Map<Class<?>, Object> beans;
    private final ContainerContext parent;

    public SingletonBeanScope(List<BeanDefinition> beanDefinitions, BeanFactory beanFactory, ContainerContext parent,
            List<BeanProcessor> beanProcessors, List<BeanPreRemoveProcessor> preRemoveProcessors)
    {
        super(beanFactory);

        this.beans = beanDefinitions.stream()
                .map(def -> Map.entry(def.getBeanClass(), def))
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));

        this.beanProcessors = beanProcessors;
        this.preRemoveProcessors = preRemoveProcessors;
        this.parent = parent;
    }

    @Override
    public void prepareStart() {
        Collection<Object> beanDefinitions = beans.values();
        if (beanProcessors == null)
        {
            throw new ContainerException("Scope was post processed already! BeanProcessors was removed");
        }
        if (beanDefinitions.stream().anyMatch(o -> !(o instanceof BeanDefinition)))
        {
            throw new ContainerException("Scope was started already! Any of beans is not BeanDefinition");
        }

        for (BeanProcessor beanProcessor : beanProcessors) {
            for (Object beanDefinition : beanDefinitions) {
                runPreConstruct(beanProcessor, (BeanDefinition)beanDefinition, parent, LOG);
            }
        }

        for (Class<?> beanClass : beans.keySet()) {
            safeCreateBean(beanClass);
        }
    }

    @Override
    public void postProcess() {
        if (beanProcessors == null)
        {
            throw new ContainerException("Scope was post processed already! BeanProcessors was removed");
        }

        for (BeanProcessor beanProcessor : beanProcessors) {
            for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
                Object createdBean = entry.getValue();
                if (createdBean instanceof BeanDefinition) {
                    throw new ContainerException("Scope has not created beans! Bean definition: " + createdBean);
                }
                if (!(createdBean instanceof ResolvedBeanData resolvedBeanData)) {
                    throw new ContainerException("Scope has beans in incorrect format: " + createdBean);
                }
                runPostConstruct(beanProcessor, resolvedBeanData, parent, LOG);
            }
        }
    }

    @Override
    public void prepareStop() {
        for (BeanPreRemoveProcessor preRemoveProcessor : preRemoveProcessors) {
            for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
                Object createdBean = entry.getValue();
                if (createdBean instanceof ResolvedBeanData resolvedBeanData) {
                    runPreRemove(preRemoveProcessor, resolvedBeanData, parent, LOG);
                }
            }
        }
    }

    @Override
    public List<Class<?>> getBeanClasses() {
        return new ArrayList<>(beans.keySet());
    }

    @Override
    public ResolvedBeanData getBean(ResolvingProperties properties) {
        Class<?> requestedClass = properties.getRequestedClass();

        Object createdBean = beans.getOrDefault(requestedClass, ResolvedBeanData.NOT_RESOLVED);
        if (createdBean instanceof BeanDefinition) {
            createdBean = safeCreateBean(requestedClass);
        }
        if (createdBean instanceof ResolvedBeanData resolvedBeanData) {
            return resolvedBeanData;
        }
        throw new ContainerException("Bean has unavailable type: " + createdBean.getClass());
    }

    private ResolvedBeanData safeCreateBean(Class<?> beanClass) {
        return (ResolvedBeanData) beans.compute(beanClass,
                (k, v) -> v instanceof BeanDefinition definition
                        ? constructBean(beanProcessors, definition, parent, LOG)
                        : v
        );
    }
}
