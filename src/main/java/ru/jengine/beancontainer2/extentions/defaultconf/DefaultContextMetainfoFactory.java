package ru.jengine.beancontainer2.extentions.defaultconf;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer2.contextmetainfo.metainfos.ModuleBasedContainerMetainfo;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.Module;

import java.util.List;

public class DefaultContextMetainfoFactory implements ContextMetainfoFactory {
    @Override
    public ContextMetainfo build(String contextName, List<Module> modules, ContainerConfiguration configuration) {
        return new ModuleBasedContainerMetainfo(modules);
    }
}
