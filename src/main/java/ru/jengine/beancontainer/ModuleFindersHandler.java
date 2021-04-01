package ru.jengine.beancontainer;

import ru.jengine.beancontainer.dataclasses.ContainerConfiguration;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleFindersHandler {
    public List<Module> findAllModules(ModuleFinder mainModuleFinder, ContainerConfiguration configuration) {
        Queue<ModuleFinder> moduleFinders = new LinkedList<>();
        List<Module> result = new LinkedList<>();
        Set<Class<?>> allFoundedModules = new HashSet<>();

        moduleFinders.add(mainModuleFinder);

        while (!moduleFinders.isEmpty()) {
            ModuleFinder moduleFinder = moduleFinders.poll();

            List<Module> foundedModules = moduleFinder.find(configuration);
            Queue<Module> mainModules = new LinkedList<>(foundedModules);

            while (!mainModules.isEmpty()) {
                Module mainModule = mainModules.poll();

                List<Module> submodules = ContainerModuleUtils.getAllSubmodules(mainModule);
                List<Module> uniqueModules = Stream.concat(Stream.of(mainModule), submodules.stream())
                        .filter(module -> !allFoundedModules.contains(module.getClass()))
                        .collect(Collectors.toList()); //TODO исправить возможные проблемы с фильтрацией

                Set<Class<?>> moduleFinderClasses = ContainerModuleUtils.getAllModuleFinders(uniqueModules);
                moduleFinderClasses.stream()
                        .map(ModuleFindersHandler::createModuleFinder)
                        .forEach(moduleFinders::add);

                uniqueModules.stream()
                        .filter(module -> !moduleFinderClasses.contains(module.getClass()))
                        .forEach(module -> {
                            result.add(module);
                            allFoundedModules.add(module.getClass());
                        });
            }
        }

        return result;
    }

    private static ModuleFinder createModuleFinder(Class<?> moduleFinderCls) {
        return BeanUtils.createComponentWithDefaultConstructor(moduleFinderCls);
    }
}
