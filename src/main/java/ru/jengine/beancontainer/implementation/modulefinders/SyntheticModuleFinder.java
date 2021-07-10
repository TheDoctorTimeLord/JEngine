package ru.jengine.beancontainer.implementation.modulefinders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFinder;
import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

public class SyntheticModuleFinder implements ModuleFinder {
    private final List<Class<?>> mainModuleClasses = new ArrayList<>();

    public void addModuleClass(Class<?> moduleClass) {
        mainModuleClasses.add(moduleClass);
    }

    @Override
    public List<Module> find(ContainerConfiguration configuration) {
        return mainModuleClasses.stream()
                .map(moduleClass -> ContainerModuleUtils.createModule(moduleClass, configuration))
                .collect(Collectors.toList());
    }
}
