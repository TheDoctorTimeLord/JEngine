package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.ModuleFinder;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.dataclasses.ModuleFindContext;
import ru.jengine.beancontainer.utils.ContainerModuleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ModuleFinderMarker
public class ClassPathModuleFinder implements ModuleFinder {
    @Override
    public List<Module> find(ModuleFindContext context) {
        ClassFinder classFinder = context.getClassFinder();
        Set<Class<?>> moduleClasses = classFinder.getAnnotatedClasses(ContainerModule.class);
        ModuleContext moduleContext = mapContext(context);
        List<Module> result = new ArrayList<>();

        for (Class<?> moduleClass : moduleClasses) {
            Module module = ContainerModuleUtils.createModule(moduleClass, moduleContext);
            result.add(module);
        }

        return result;
    }
}
