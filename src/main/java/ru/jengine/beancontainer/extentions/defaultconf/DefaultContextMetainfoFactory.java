package ru.jengine.beancontainer.extentions.defaultconf;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.contextmetainfo.metainfos.ModuleBasedContainerMetainfo;
import ru.jengine.beancontainer.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer.modules.Module;

import java.util.List;

public class DefaultContextMetainfoFactory implements ContextMetainfoFactory {
    @Override
    public ContextMetainfo build(String contextName, List<Module> modules, ContainerConfiguration configuration) {
        return new ModuleBasedContainerMetainfo(modules);
    }
}
