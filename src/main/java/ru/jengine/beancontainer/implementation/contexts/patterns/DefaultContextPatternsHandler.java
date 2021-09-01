package ru.jengine.beancontainer.implementation.contexts.patterns;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContainerMultiContext;
import ru.jengine.beancontainer.ContextPattern;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.InitializableContextPatternHandler;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.implementation.factories.SelectiveAutowireConfigurableBeanFactories;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.utils.CollectionUtils;

public class DefaultContextPatternsHandler implements InitializableContextPatternHandler {
    private final Map<String, ContextPattern> patterns = new ConcurrentHashMap<>();
    private final ContainerMultiContext multiContext;

    public DefaultContextPatternsHandler(ContainerMultiContext multiContext) {
        this.multiContext = multiContext;
    }

    @Override
    public void initialize() {
        patterns.entrySet().stream()
                .filter(entry -> entry.getValue().needLoadOnContainerInitialize())
                .forEach(entry -> loadContext(entry.getKey()));
    }

    @Override
    public void registerPattern(String patternName, ContextPattern contextPattern) throws ContainerException {
        synchronized (this) {
            if (patterns.containsKey(patternName)) {
                throw new ContainerException(
                        "Context [" + patternName + "] was loaded. Stop loading [" + contextPattern + "]");
            }

            patterns.put(patternName, contextPattern);
        }
    }

    @Override
    public void loadContext(String patternName) { //TODO синхронизовать
        if (patternWasLoaded(patternName)) {
            return;
        }

        ContainerContext newContext = createContext(patternName);
        preProcessBeans(newContext);

        multiContext.registerContext(patternName, newContext); //TODO отделить эту часть от prepareBeans, чтобы загружать взаимозависимые контексты

        newContext.prepareBeans();
    }

    @Override
    public void loadContexts(List<String> patternNames) { //TODO синхронизовать
        Map<String, ContainerContext> newContexts = patternNames.stream()
                .filter(this::patternWasLoaded)
                .collect(Collectors.toMap(name -> name, this::createContext));

        Collection<ContainerContext> contexts = newContexts.values();
        contexts.forEach(this::preProcessBeans);
        contexts.forEach(ContainerContext::prepareBeans);
        newContexts.forEach(multiContext::registerContext);
    }

    private boolean patternWasLoaded(String patternName) {
        ContextPattern pattern = patterns.get(patternName);
        return pattern != null && pattern.wasLoaded();
    }

    private ContainerContext createContext(String patternName) {
        ContextPattern pattern = patterns.get(patternName);

        if (pattern == null) {
            throw new ContainerException("Context pattern [" + patternName + "] was not loaded");
        }

        pattern.setLoaded(true);

        List<String> beanSources = pattern.getBeanSources();
        loadAllSourceContextsWithWrapException(patternName, beanSources);

        List<String> sources = CollectionUtils.add(beanSources, patternName, Constants.INFRASTRUCTURE_CONTEXT);
        return pattern.buildContext(createBeanFactoryWithSource(sources));
    }

    private BeanFactory createBeanFactoryWithSource(List<String> beanSources) {
        SelectiveAutowireConfigurableBeanFactories beanFactory =
                new SelectiveAutowireConfigurableBeanFactories(multiContext, beanSources);

        ContainerContext infrastructureContext = multiContext.getContext(Constants.INFRASTRUCTURE_CONTEXT);
        if (infrastructureContext != null) {
            beanFactory.configure(infrastructureContext);
        } else {
            //TODO LOG IT!!!!
        }

        return beanFactory;
    }

    private void loadAllSourceContextsWithWrapException(String patternName, List<String> beanSources) {
        try {
            loadAllSourceContexts(beanSources);
        } catch (ContainerException ex) {
            throw new ContainerException("Any source for pattern [" + patternName + "] not registered", ex);
        }
    }

    private void loadAllSourceContexts(List<String> beanSources) {
        for (String beanSource : beanSources) {
            ContextPattern pattern = patterns.get(beanSource);

            if (pattern == null) {
                throw new ContainerException("Pattern [" + beanSource + "] not registered");
            }

            if (pattern.wasLoaded()) {
                continue;
            }

            loadContext(beanSource);
        }
    }

    private void preProcessBeans(ContainerContext newContext) {
        List<ContextPreProcessor> preProcessors = BeanUtils.getBeanAsList(
                multiContext.getBean(Constants.INFRASTRUCTURE_CONTEXT, ContextPreProcessor.class));

        newContext.preProcessBeans(preProcessors);
    }
}
