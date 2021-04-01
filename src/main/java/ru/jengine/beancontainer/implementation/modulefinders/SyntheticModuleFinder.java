package ru.jengine.beancontainer.implementation.modulefinders;

import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFinder;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SyntheticModuleFinder implements ModuleFinder {
    private final List<Class<?>> mainModuleClasses = new ArrayList<>();

    public void addModuleClass(Class<?> moduleClass) {
        mainModuleClasses.add(moduleClass);
    }

    @Override
    public List<Module> find(ContainerConfiguration configuration) {
        return mainModuleClasses.stream()
                .map(ContainerModuleUtils::createModule)
                .collect(Collectors.toList());
    }
}
