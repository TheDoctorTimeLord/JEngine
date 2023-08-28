package ru.jengine.beancontainer2.containercontext.scopes;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.BeanExtractor;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer2.extentions.BeanProcessor;

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
                if (!(createdBean instanceof BeanDefinition)) {
                    runPostConstruct(beanProcessor, createdBean, entry.getKey(), parent, LOG);
                }
            }
        }
    }

    @Override
    public void prepareStop() {
        for (BeanPreRemoveProcessor preRemoveProcessor : preRemoveProcessors) {
            for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
                Object createdBean = entry.getValue();
                if (!(createdBean instanceof BeanDefinition)) {
                    runPreRemove(preRemoveProcessor, createdBean, entry.getKey(), parent, LOG);
                }
            }
        }
    }

    @Override
    public List<Class<?>> getBeanClasses() {
        return new ArrayList<>(beans.keySet());
    }

    @Nullable
    @Override
    public Object getBean(ResolvingProperties properties) {
        Class<?> requestedClass = properties.getRequestedClass();

        Object createdBean = beans.getOrDefault(requestedClass, BeanExtractor.NOT_RESOLVED);
        if (createdBean instanceof BeanDefinition) {
            createdBean = safeCreateBean(requestedClass);
        }

        return createdBean;
    }

    private Object safeCreateBean(Class<?> beanClass) {
        return beans.compute(beanClass,
                (k, v) -> v instanceof BeanDefinition definition
                        ? constructProcessing(createBean(definition, LOG), k)
                        : v
        );
    }

    private Object constructProcessing(Object createdBean, Class<?> beanClass) {
        for (BeanProcessor beanProcessor : beanProcessors) {
            createdBean = runConstruct(beanProcessor, createdBean, beanClass, parent, LOG);
        }
        return createdBean;
    }
}
