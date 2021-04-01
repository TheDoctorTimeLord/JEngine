package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;

import java.util.List;

public interface ModuleFinder {
    List<Module> find(ContainerConfiguration context);
}
