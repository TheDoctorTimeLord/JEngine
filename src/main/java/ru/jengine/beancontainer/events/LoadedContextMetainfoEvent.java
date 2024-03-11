package ru.jengine.beancontainer.events;

import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.statepublisher.ContainerEvent;

public record LoadedContextMetainfoEvent(String contextName, ContextMetainfo metainfo, ContainerContext context)
        implements ContainerEvent { }
