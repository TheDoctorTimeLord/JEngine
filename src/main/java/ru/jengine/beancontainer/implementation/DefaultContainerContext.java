package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultContainerContext implements ContainerContext {
    private BeanFactory beanFactory;
    private List<BeanDefinition> beanDefinitions;
    private final Map<Class<?>, BeanContext> beans = new ConcurrentHashMap<>();

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;
        this.beanDefinitions = ContainerModuleUtils.extractAllBeanDefinition(modules);
        beanDefinitions.forEach(definition -> beans.put(definition.getBeanClass(), new BeanContext()));
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
