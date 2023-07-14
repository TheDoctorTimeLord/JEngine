package ru.jengine.beancontainer2.contextmetainfo;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.modules.Module;

import java.util.List;

public interface ContextMetainfoFactory {
    ContextMetainfo build(List<Module> modules, ContainerConfiguration configuration);
}
