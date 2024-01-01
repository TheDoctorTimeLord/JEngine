package ru.jengine.beancontainer.contextmetainfo;

import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.beanfactory.BeanFactoryWithSources;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.events.RemoveContextEvent;
import ru.jengine.beancontainer.events.StartingInitializeContextsPhase;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.statepublisher.ContainerListener;
import ru.jengine.beancontainer.statepublisher.ContainerEventDispatcher;

import java.util.*;
import java.util.stream.Stream;

public class ContextMetainfoManager {
    private final ContainerConfiguration configuration;
    private final ContainerContextFacade contextFacade;
    private final ContainerEventDispatcher eventDispatcher;
    private final Map<String, ContextMetainfo> metainfoByName = new HashMap<>();

    public ContextMetainfoManager(ContainerConfiguration configuration, ContainerContextFacade contextFacade,
            ContainerEventDispatcher eventDispatcher)
    {
        this.configuration = configuration;
        this.contextFacade = contextFacade;
        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerListener(new OnInitializeListener());
    }

    public void registerContextMetainfo(String metainfoName, ContextMetainfo metainfo) {
        if (metainfoByName.containsKey(metainfoName)) {
            throw new ContainerException("ContextMetainfo with name [%s] has been registered already".formatted(metainfoName));
        }

        metainfoByName.put(metainfoName, metainfo);
    }

    public List<String> loadContainerMetainfo(String metainfoName, String newContextName, ContainerState state) {
        return loadContainerMetainfo(extractMetainfo(metainfoName), newContextName, state);
    }

    private List<String> loadContainerMetainfo(ContextMetainfo metainfo, String newContextName, ContainerState state) {
        if (contextFacade.hasContext(newContextName)) {
            return List.of();
        }

        List<String> beanSources = metainfo.getBeanSources();
        String[] extendedBeanSources = Stream.concat(
                Stream.concat(
                        Stream.of(newContextName),
                        beanSources.stream()
                ),
                configuration.getPreloadedContextNames().stream()
        ).distinct().toArray(String[]::new);

        BeanFactoryWithSources beanFactory = new BeanFactoryWithSources(contextFacade, extendedBeanSources);
        ContainerContext containerContext = configuration.getContainerContextFactory()
                .build(newContextName, metainfo, beanFactory, state);

        eventDispatcher.registerListener(new OnRemoveContextListener(metainfo, newContextName));
        contextFacade.registerContext(newContextName, containerContext);

        List<String> loadedContexts = new ArrayList<>();
        for (String beanSource : beanSources) {
            loadedContexts.addAll(loadContainerMetainfo(extractMetainfo(beanSource), beanSource, state));
        }
        loadedContexts.add(newContextName);

        return loadedContexts;
    }

    private ContextMetainfo extractMetainfo(String metainfoName) {
        ContextMetainfo metainfo = metainfoByName.get(metainfoName);
        if (metainfo == null) {
            throw new ContainerException("Context metainfo with name [%s] not found".formatted(metainfoName));
        }
        return metainfo;
    }

    private class OnInitializeListener implements ContainerListener<StartingInitializeContextsPhase> {
        @Override
        public Class<StartingInitializeContextsPhase> getListenedEventClass() {
            return StartingInitializeContextsPhase.class;
        }
        @Override
        public void handle(StartingInitializeContextsPhase event, ContainerState state) {
            List<String> loadedContexts = new ArrayList<>();

            //TODO добавить топологическую сортировку для выявления циклических зависимостей

            for (Map.Entry<String, ContextMetainfo> metainfo : ContextMetainfoManager.this.metainfoByName.entrySet()) {
                if (metainfo.getValue().needLoadOnContainerInitialize()) {
                    loadedContexts.addAll(loadContainerMetainfo(metainfo.getValue(), metainfo.getKey(), state));
                }
            }

            contextFacade.constructBeans(loadedContexts);

            removeListener(state);
        }
    }

    private static class OnRemoveContextListener implements ContainerListener<RemoveContextEvent> {
        private final ContextMetainfo contextMetainfo;
        private final String contextName;

        private OnRemoveContextListener(ContextMetainfo contextMetainfo, String contextName) {
            this.contextMetainfo = contextMetainfo;
            this.contextName = contextName;
        }

        @Override
        public Class<RemoveContextEvent> getListenedEventClass() {
            return RemoveContextEvent.class;
        }

        @Override
        public void handle(RemoveContextEvent event, ContainerState containerState) {
            if (contextMetainfo.getBeanSources().contains(event.getContextName())) {
                containerState.getContainerContextFacade().removeContext(contextName);
                removeListener(containerState);
            }
        }
    }
}
