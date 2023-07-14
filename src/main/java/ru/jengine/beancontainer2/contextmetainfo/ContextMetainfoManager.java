package ru.jengine.beancontainer2.contextmetainfo;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.containercontext.ContainerContextFacade;
import ru.jengine.beancontainer2.exceptions.ContainerException;

import java.util.HashMap;
import java.util.Map;

public class ContextMetainfoManager {
    private final ContainerConfiguration configuration;
    private final ContainerContextFacade contextFacade;
    private final Map<String, ContextMetainfo> metainfoByName = new HashMap<>();

    public ContextMetainfoManager(ContainerConfiguration configuration, ContainerContextFacade contextFacade) {
        this.configuration = configuration;
        this.contextFacade = contextFacade;
    }

    public void registerContextMetainfo(String contextName, ContextMetainfo metainfo) {
        if (metainfoByName.containsKey(contextName)) {
            throw new ContainerException("ContextMetainfo with name [%s] has been registered already".formatted(contextName));
        }

        metainfoByName.put(contextName, metainfo);
    }
}
