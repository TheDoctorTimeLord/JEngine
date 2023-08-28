package ru.jengine.beancontainer2.contextmetainfo;

import ru.jengine.beancontainer2.ContainerState;
import ru.jengine.beancontainer2.beanfactory.BeanFactoryWithSources;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer2.events.StartingInitializeContextsPhase;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.statepublisher.ContainerListener;
import ru.jengine.beancontainer2.statepublisher.ContainerStatePublisher;

import java.util.*;
import java.util.stream.Stream;

public class ContextMetainfoManager {
    private final ContainerConfiguration configuration;
    private final ContainerContextFacade contextFacade;
    private final Map<String, ContextMetainfo> metainfoByName = new HashMap<>();

    public ContextMetainfoManager(ContainerConfiguration configuration, ContainerContextFacade contextFacade,
            ContainerStatePublisher publisher)
    {
        this.configuration = configuration;
        this.contextFacade = contextFacade;

        publisher.registerListener(new OnInitializeListener());
    }

    public void registerContextMetainfo(String metainfoName, ContextMetainfo metainfo) {
        if (metainfoByName.containsKey(metainfoName)) {
            throw new ContainerException("ContextMetainfo with name [%s] has been registered already".formatted(metainfoName));
        }

        metainfoByName.put(metainfoName, metainfo);
    }

    public void unregisterContextMetainfo(String metainfoName) {
        metainfoByName.remove(metainfoName);
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
        ).toArray(String[]::new);

        BeanFactoryWithSources beanFactory = new BeanFactoryWithSources(contextFacade, extendedBeanSources);
        ContainerContext containerContext = configuration.getContainerContextFactory().build(metainfo, beanFactory, state);

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
        }
    }
}
