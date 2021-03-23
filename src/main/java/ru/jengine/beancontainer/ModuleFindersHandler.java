package ru.jengine.beancontainer;

import ru.jengine.beancontainer.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer.dataclasses.ModuleFindContext;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleFindersHandler {
    private final ClassFinder classFinder;

    public ModuleFindersHandler(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    public List<Module> findAllModules(Class<?> mainModule) {
        List<Module> modulesInProject = ContainerModuleUtils.scanClassPathAndFindModules(mainModule, classFinder);
        List<Module> externalModules = findExternalModules();
        return concatModules(modulesInProject, externalModules);
    }

    private List<Module> findExternalModules() {
        Set<Class<?>> moduleFinderClasses = classFinder.getAnnotatedClasses(ModuleFinderMarker.class);
        List<Module> result = new ArrayList<>();
        for (Class<?> cls : moduleFinderClasses) {
            ModuleFinder finder = BeanUtils.createComponentWithDefaultConstructor(cls);
            List<Module> foundedModules = finder.find(new ModuleFindContext(classFinder));
            result.addAll(foundedModules);
        }
        return result;
    }

    private static List<Module> concatModules(List<Module> first, List<Module> second) {
        Set<Class<?>> firstModuleClasses = first.stream()
                .map(Object::getClass)
                .collect(Collectors.toSet());

        List<Module> result = new ArrayList<>(first);

        second.stream()
                .filter(module -> !firstModuleClasses.contains(module.getClass()))
                .forEach(result::add);

        return result;
    }
}
