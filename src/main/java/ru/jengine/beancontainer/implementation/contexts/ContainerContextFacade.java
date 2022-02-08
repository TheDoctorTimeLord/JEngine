package ru.jengine.beancontainer.implementation.contexts;

import static ru.jengine.utils.CollectionUtils.groupBy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.service.Constants.Contexts;
import ru.jengine.beancontainer.utils.BeanUtils;

public class ContainerContextFacade implements ContainerMultiContext {
    private final Map<String, ContainerContext> internalContexts = new ConcurrentHashMap<>();
    private final Map<String, ContainerContext> externalContexts = new ConcurrentHashMap<>();
    private BeanFactory beanFactory;

    @Override
    public void initialize(List<Module> modules, BeanFactory factory) {
        this.beanFactory = factory;

        Map<String, List<Module>> modulesByContext = groupBy(modules, Module::getContextName);
        modulesByContext.forEach(this::registerContext);
    }

    @Override
    public void preProcessBeans(List<ContextPreProcessor> contextPreProcessors) {
        internalContexts.forEach((key, value) -> value.preProcessBeans(contextPreProcessors));
    }

    @Override
    public void prepareBeans() {
        internalContexts.forEach((key, value) -> value.prepareBeans());
    }

    @Override
    public void reload() {
        Stream.concat(externalContexts.values().stream(), internalContexts.values().stream())
                .forEach(ContainerContext::reload);
    }

    @Override
    public void prepareToRemove() {
        Stream.concat(externalContexts.values().stream(), internalContexts.values().stream())
                .forEach(ContainerContextFacade::prepareToRemove);
    }

    @Override
    public BeanContext getBean(Class<?> beanClass) {
        boolean asCollection = beanClass.isInterface();
        BeanContext bean =
                BeanUtils.findAppropriateValueBean(this, beanClass, getAllContextsNames(), asCollection);

        if (bean != null && bean.isCollectionBean()) {
            Collection<?> elements = bean.getBean();
            if (elements.size() == 1) {
                return new BeanContext(((Collection<?>)bean.getBean()).iterator().next(), beanClass);
            } else if (elements.isEmpty() && !beanClass.isAssignableFrom(Collection.class)) {
                return null;
            }
        }
        return bean;
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
    public BeanContext getBean(String contextName, Class<?> beanClass) {
        ContainerContext context = getContext(contextName);
        return context == null ? null : context.getBean(beanClass);
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
    @Nullable
    public ContainerContext getContext(String name) {
        ContainerContext context = externalContexts.get(name);
        return context == null ? internalContexts.get(name) : context;
    }

    @Override
    public void registerContext(String name, ContainerContext context) {
        externalContexts.put(name, context);
    }

    @Override
    public void removeContext(String name) {
        if (Contexts.INFRASTRUCTURE_CONTEXT.equals(name)) {
            throw new ContainerException("Can not remove infrastructure context");
        }

        ContainerContext removedFromInternal = internalContexts.remove(name);
        ContainerContext removedFromExternal = externalContexts.remove(name);

        prepareToRemove(removedFromInternal);
        prepareToRemove(removedFromExternal);
    }

    @Override
    public void reloadContext(String name) {
        ContainerContext reloadedFromInternal = internalContexts.get(name);
        ContainerContext reloadedFromExternal = externalContexts.get(name);

        if (reloadedFromInternal != null) {
            reloadedFromInternal.reload();
        }
        if (reloadedFromExternal != null) {
            reloadedFromExternal.reload();
        }
    }

    private static void prepareToRemove(ContainerContext context) {
        if (context == null) {
            return;
        }

        context.prepareToRemove();
    }

    private void registerContext(String contextName, List<Module> initializedModules) {
        ContainerContext context = new DefaultContainerContext();
        internalContexts.put(contextName, context);
        context.initialize(initializedModules, beanFactory);
    }

    private Iterable<ContainerContext> getAllContextsWithoutNames() {
        return Stream.concat(internalContexts.values().stream(), externalContexts.values().stream())
                .collect(Collectors.toList());
    }

    private Collection<String> getAllContextsNames() {
        return Stream.concat(internalContexts.keySet().stream(), externalContexts.keySet().stream())
                .collect(Collectors.toList());
    }
}
