package ru.jengine.beancontainer.intstructure.pac2;

import ru.jengine.beancontainer.annotations.ModuleFinderMarker;
import ru.jengine.beancontainer.modules.Module;
import ru.jengine.beancontainer.modulefinders.ModuleFinder;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.intstructure.pac3.ModuleByFinder1;
import ru.jengine.beancontainer.intstructure.pac3.ModuleByFinder2;
import ru.jengine.beancontainer.modules.ModuleFactory;

import java.util.List;

@ModuleFinderMarker
public class ModuleFinder1 implements ModuleFinder {
    @Override
    public List<Module> find(ContainerConfiguration configuration) {
        ModuleFactory moduleFactory = configuration.getModuleFactory();
        return List.of(
                moduleFactory.createAnnotatedModule(ModuleByFinder1.class, configuration),
                moduleFactory.createAnnotatedModule(ModuleByFinder2.class, configuration),
                moduleFactory.createAnnotatedModule(ModuleByFinder1.class, configuration)
        );
    }
}
