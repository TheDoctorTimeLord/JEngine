package ru.jengine.beancontainer2.configuration;

import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.ContainerContextFactory;
import ru.jengine.beancontainer2.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer2.modules.ModuleFactory;
import ru.jengine.beancontainer2.modules.Module;

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
