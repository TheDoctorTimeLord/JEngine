package ru.jengine.beancontainer.implementation.contexts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.InterfaceLocator;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.implementation.InterfaceLocatorByResolver;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

public class DefaultContainerContext implements ContainerContext {
    private BeanFactory beanFactory;
    private List<BeanDefinition> beanDefinitions;
    private final Map<Class<?>, BeanContext> beans = new ConcurrentHashMap<>();
    private final InterfaceLocator interfaceLocator = new InterfaceLocatorByResolver(cls -> getBean(cls).getBean());

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;
        this.beanDefinitions = ContainerModuleUtils.extractAllBeanDefinition(modules);
        beanDefinitions.forEach(this::registerDefinition);
    }

    private void registerDefinition(BeanDefinition definition) {
        Class<?> beanClass = definition.getBeanClass();

        beans.put(beanClass, new BeanContext(beanClass));
        interfaceLocator.registerClassInterfaces(beanClass);
    }

    @Override
    public void preProcessBeans(List<ContextPreProcessor> contextPreProcessors) {
        beanDefinitions.removeIf(beanDefinition -> {
            contextPreProcessors.forEach(preProcessor -> preProcessor.preProcess(beanDefinition));
            return beanDefinition.mustRemovedAfterPreProcess();
        });
    }

    @Override
    public void prepareBeans() {
        prepareSingletons();
    }

    private void prepareSingletons() {
        beanDefinitions.stream()
                .filter(BeanDefinition::isSingleton)
                .forEach(beanDefinition -> getBean(beanDefinition.getBeanClass()));
    }

    @Override
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
        BeanContext context = beans.get(beanClass);
        if (context == null) {
            return null;
        }
        if (context.getBean() != null) {
            return context;
        }
        BeanContext beanContext = beanFactory.buildBean(beanClass);
        context.setInstance(beanContext.getBean());

        return context;
    }

    @Override
    public boolean containsBean(Class<?> beanClass) {
        return beans.containsKey(beanClass);
    }

    @Override
    public void deleteBean(Object bean) {

    }
}
