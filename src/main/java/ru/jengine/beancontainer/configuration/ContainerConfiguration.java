package ru.jengine.beancontainer.configuration;

import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.ContainerContextFactory;
import ru.jengine.beancontainer.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer.modules.ModuleFactory;
import ru.jengine.beancontainer.modules.Module;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface ContainerConfiguration {
    Map<String, List<Module>> getExternalSetModules();
    Class<?> getMainModuleClass();
    Supplier<ClassFinder> getClassFinderFactory();
    ModuleFactory getModuleFactory();
    ContextMetainfoFactory getContextMetainfoFactory();
    ContainerContextFactory getContainerContextFactory();
    List<String> getPreloadedContextNames();
    BeanCreationScopeResolver getBeanCreationScopeResolver();

    static DefaultContainerConfigurationBuilder builder(Class<?> mainModuleClass) {
        return new DefaultContainerConfigurationBuilder(mainModuleClass);
    }
}
