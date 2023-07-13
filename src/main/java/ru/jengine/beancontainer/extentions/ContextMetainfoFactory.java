package ru.jengine.beancontainer.extentions;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.modules.Module;

import java.util.List;

public interface ContextMetainfoFactory {
    ContextMetainfo build(String contextName, List<Module> modules, ContainerConfiguration configuration);
}
