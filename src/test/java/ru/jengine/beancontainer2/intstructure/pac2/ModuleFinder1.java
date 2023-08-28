package ru.jengine.beancontainer2.intstructure.pac2;

import ru.jengine.beancontainer2.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer2.modules.Module;
import ru.jengine.beancontainer2.modulefinders.ModuleFinder;
import ru.jengine.beancontainer2.configuration.ContainerConfiguration;
import ru.jengine.beancontainer2.intstructure.pac3.ModuleByFinder1;
import ru.jengine.beancontainer2.intstructure.pac3.ModuleByFinder2;
import ru.jengine.beancontainer2.modules.ModuleFactory;

import java.util.List;

@ModuleFinderMarker
public class ModuleFinder1 implements ModuleFinder {
    @Override
    public List<Module> find(ContainerConfiguration configuration) {
        ModuleFactory moduleFactory = configuration.getModuleFactory();
        return List.of(
                moduleFactory.createModule(ModuleByFinder1.class, configuration),
                moduleFactory.createModule(ModuleByFinder2.class, configuration),
                moduleFactory.createModule(ModuleByFinder1.class, configuration)
        );
    }
}
