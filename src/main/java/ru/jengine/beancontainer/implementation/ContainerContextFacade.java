package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContainerContextFacade implements ContainerMultiContext {
    private final Map<String, ContainerContext> internalContexts = new ConcurrentHashMap<>();
    private final Map<String, ContainerContext> externalContexts = new ConcurrentHashMap<>();
    private BeanFactory beanFactory;

    private Iterable<ContainerContext> getAllContextsWithoutNames() {
        return Stream.concat(internalContexts.values().stream(), externalContexts.values().stream())
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;

        Map<String, List<Module>> modulesByContext = groupByContext(modules);
        modulesByContext.forEach(this::registerContext);
    }

    @Override
    public void prepareBeans() {
        internalContexts.forEach((key, value) -> value.prepareBeans());
    }

    private static Map<String, List<Module>> groupByContext(List<Module> modules) {
        Map<String, List<Module>> result = new HashMap<>();

        for (Module module : modules) {
            String contextName = ContainerModuleUtils.extractContextForModule(module);
            if (!result.containsKey(contextName)) {
                result.put(contextName, new ArrayList<>());
            }
            result.get(contextName).add(module);
        }

        return result;
    }

    @Override
    public BeanContext getBean(Class<?> beanClass) {
        for (ContainerContext context : getAllContextsWithoutNames()) {
            BeanContext beanContext = context.getBean(beanClass);
            if (beanContext != null) {
                return beanContext;
            }
        }
        return null;
    }

    @Override
    public boolean containsBean(Class<?> beanClass) {
        for (ContainerContext context : getAllContextsWithoutNames()) {
            if (context.containsBean(beanClass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteBean(Object bean) {
        getAllContextsWithoutNames().forEach(context -> context.deleteBean(bean));
    }

    @Override
    public BeanContext getBean(String contextName, Class<?> beanClass) {
        ContainerContext context = internalContexts.get(contextName);
        if (context == null) {
            context = externalContexts.get(contextName);
            return context == null ? null : context.getBean(beanClass);
        }

        return context.getBean(beanClass);
    }

    @Override
    public boolean containsBean(String contextName, Class<?> beanClass) {
        ContainerContext context = internalContexts.get(contextName);
        if (context == null) {
            context = externalContexts.get(contextName);
            return context != null && context.containsBean(beanClass);
        }
        return context.containsBean(beanClass);
    }

    @Override
    public void deleteBean(String contextName, Object bean) {
        ContainerContext context = internalContexts.get(contextName);
        if (context != null) {
            context.deleteBean(bean);
        } else {
            context = externalContexts.get(contextName);
            if (context != null) {
                context.deleteBean(bean);
            }
        }
    }

    @Override
    public void registerContext(String name, ContainerContext context) {
        externalContexts.put(name, context);
    }

    private void registerContext(String contextName, List<Module> initializedModules) {
        ContainerContext context = new DefaultContainerContext();
        internalContexts.put(contextName, context);
        context.initialize(initializedModules, beanFactory);
    }
}
