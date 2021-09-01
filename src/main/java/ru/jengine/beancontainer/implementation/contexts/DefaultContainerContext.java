package ru.jengine.beancontainer.implementation.contexts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.ConfigurableBeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.InterfaceLocator;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.implementation.InterfaceLocatorByResolver;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

public class DefaultContainerContext implements ContainerContext {
    private BeanFactory beanFactory;
    private Map<Class<?>, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();
    private final InterfaceLocator interfaceLocator = new InterfaceLocatorByResolver(cls -> getBean(cls).getBean());

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;
        this.beanDefinitions = ContainerModuleUtils.extractAllBeanDefinition(modules)
                .stream()
                .distinct()
                .collect(Collectors.toMap(BeanDefinition::getBeanClass, definition -> definition));
        beanDefinitions.values().forEach(this::registerDefinition);
    }

    private void registerDefinition(BeanDefinition definition) {
        Class<?> beanClass = definition.getBeanClass();
        interfaceLocator.registerClassInterfaces(beanClass);
    }

    @Override
    public void preProcessBeans(List<ContextPreProcessor> contextPreProcessors) {
        List<Class<?>> removingBeanDefinitions = new ArrayList<>();

        for (BeanDefinition definition : beanDefinitions.values()) {
            contextPreProcessors.forEach(preProcessor -> preProcessor.preProcess(definition));
            if (definition.mustRemovedAfterPreProcess()) {
                removingBeanDefinitions.add(definition.getBeanClass());
            }
        }

        for (Class<?> beanClass : removingBeanDefinitions) {
            beanDefinitions.remove(beanClass);
        }
    }

    @Override
    public void prepareBeans() {
        beanDefinitions.values().stream()
                .map(BeanDefinition::getBeanFactoryStrategy)
                .filter(BeanFactoryStrategy::needPrepare)
                .forEach(strategy -> strategy.getBean(beanFactory));
    }

    @Override
    public void reload() {
        prepareToRemove();
        prepareBeans();
    }

    @Override
    public void prepareToRemove() {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            ConfigurableBeanFactory factory = (ConfigurableBeanFactory)beanFactory;
            beanDefinitions.values().stream()
                    .map(bd -> bd.getBeanFactoryStrategy().getBean(beanFactory)) //TODO разобраться со стратегиями, которые не синглтон
                    .forEach(factory::beforeRemove);
        }
        beanDefinitions.values().forEach(bd -> bd.getBeanFactoryStrategy().clear());
    }

    @Override
    @Nullable
    public BeanContext getBean(Class<?> beanClass) {
        return beanClass.isInterface()
                ? getImplementationsInterface(beanClass)
                : getCommonBean(beanClass);
    }

    private BeanContext getImplementationsInterface(Class<?> beanClass) {
        List<Object> implementations = interfaceLocator.getAllImplementations(beanClass);
        return implementations.size() == 1
                ? new BeanContext(implementations.get(0), beanClass)
                : new BeanContext(implementations, beanClass);
    }

    private BeanContext getCommonBean(Class<?> beanClass) {
        BeanDefinition definition = beanDefinitions.get(beanClass);
        return definition == null ? null : definition.getBeanFactoryStrategy().getBean(beanFactory);
    }

    @Override
    public boolean containsBean(Class<?> beanClass) {
        return beanDefinitions.containsKey(beanClass);
    }
}
