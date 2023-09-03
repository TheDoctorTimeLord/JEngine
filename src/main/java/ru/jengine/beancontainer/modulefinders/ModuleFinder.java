package ru.jengine.beancontainer.modulefinders;

import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.modules.Module;

import java.util.List;

public interface ModuleFinder {
    List<Module> find(ContainerConfiguration configuration);
}
