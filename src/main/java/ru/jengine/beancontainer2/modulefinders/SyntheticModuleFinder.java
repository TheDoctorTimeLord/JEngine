package ru.jengine.beancontainer2.modulefinders;

import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.modules.ModuleFactory;

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
        ModuleFactory moduleFactory = configuration.getModuleFactory();
        return mainModuleClasses.stream()
                .map(moduleClass -> moduleFactory.createAnnotatedModule(moduleClass, configuration))
                .collect(Collectors.toList());
    }
}
