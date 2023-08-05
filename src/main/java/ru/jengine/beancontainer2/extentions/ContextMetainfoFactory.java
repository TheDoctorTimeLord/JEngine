package ru.jengine.beancontainer2.extentions;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.modules.Module;

import java.util.List;

public interface ContextMetainfoFactory {
    ContextMetainfo build(String contextName, List<Module> modules, ContainerConfiguration configuration);
}
