package ru.jengine.beancontainer2.modulefinders;

import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.modules.Module;

import java.util.List;

public interface ModuleFinder {
    List<Module> find(ContainerConfiguration configuration);
}
