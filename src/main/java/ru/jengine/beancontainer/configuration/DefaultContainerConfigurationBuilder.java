package ru.jengine.beancontainer.configuration;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.classfinders.ClassPathScanner;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.ContainerContextFactory;
import ru.jengine.beancontainer.extentions.ContextMetainfoFactory;
import ru.jengine.beancontainer.extentions.defaultconf.DefaultBeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.defaultconf.DefaultContainerContextFactory;
import ru.jengine.beancontainer.extentions.defaultconf.DefaultContextMetainfoFactory;
import ru.jengine.beancontainer.modules.ModuleFactory;
import ru.jengine.beancontainer.modules.CustomModule;
import ru.jengine.beancontainer.modules.DefaultModuleFactory;
import ru.jengine.beancontainer.modules.Module;

import java.util.*;
import java.util.function.Supplier;

public class DefaultContainerConfigurationBuilder {
    private final Map<String, List<Object>> externalBeans = new HashMap<>();
    private final List<Module> externalSetModules = new ArrayList<>();
    private final Class<?> mainModuleClass;
    private Supplier<ClassFinder> classFinderFactory = ClassPathScanner::new;
    private ModuleFactory moduleFactory = new DefaultModuleFactory();
    private ContextMetainfoFactory contextMetainfoFactory = new DefaultContextMetainfoFactory();
    private ContainerContextFactory containerContextFactory = new DefaultContainerContextFactory();
    private BeanCreationScopeResolver beanCreationScopeResolver = new DefaultBeanCreationScopeResolver();
    private List<String> preloadedContextNames = List.of(
            Constants.Contexts.EXTERNAL_BEANS_CONTEXT,
            Constants.Contexts.INFRASTRUCTURE_CONTEXT
    );

    DefaultContainerConfigurationBuilder(Class<?> mainModuleClass) {
        this.mainModuleClass = mainModuleClass;
    }

    public DefaultContainerConfigurationBuilder addBeans(Object... beans) {
        externalBeans.computeIfAbsent(Constants.Contexts.EXTERNAL_BEANS_CONTEXT, k -> new ArrayList<>())
                .addAll(Arrays.asList(beans));
        return this;
    }

    public DefaultContainerConfigurationBuilder addBeans(String context, Object... beans) {
        externalBeans.computeIfAbsent(context, k -> new ArrayList<>()).addAll(Arrays.asList(beans));
        return this;
    }

    public DefaultContainerConfigurationBuilder addExternalModule(Module module) {
        externalSetModules.add(module);
        return this;
    }

    public DefaultContainerConfigurationBuilder classFinderFactory(Supplier<ClassFinder> classFinderFactory) {
        this.classFinderFactory = classFinderFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder moduleFactory(ModuleFactory moduleFactory) {
        this.moduleFactory = moduleFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder contextMetainfoFactory(ContextMetainfoFactory contextMetainfoFactory) {
        this.contextMetainfoFactory = contextMetainfoFactory;
        return this;
    }

    public DefaultContainerConfigurationBuilder containerContextFactory(ContainerContextFactory containerContextFactory) {
        this.containerContextFactory = containerContextFactory;
        return this;
    }

    public void beanCreationScopeResolver(BeanCreationScopeResolver beanCreationScopeResolver) {
        this.beanCreationScopeResolver = beanCreationScopeResolver;
    }

    public DefaultContainerConfigurationBuilder preloadedContextNames(List<String> preloadedContextNames) {
        this.preloadedContextNames = preloadedContextNames;
        return this;
    }

    public ContainerConfiguration build() {
        Map<String, List<Module>> externalModules = unionModules();

        return new DefaultContainerConfiguration(externalModules, mainModuleClass, classFinderFactory, moduleFactory,
                contextMetainfoFactory, containerContextFactory, beanCreationScopeResolver, preloadedContextNames);
    }

    private Map<String, List<Module>> unionModules() {
        Map<String, List<Module>> externalModules = new HashMap<>();

        for (Map.Entry<String, List<Object>> entry : externalBeans.entrySet()) {
            CustomModule customModule = new CustomModule(entry.getKey(), entry.getValue());
            externalModules.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(customModule);
        }

        for (Module externalSetModule : externalSetModules) {
            externalModules.computeIfAbsent(externalSetModule.getContextName(), k -> new ArrayList<>())
                    .add(externalSetModule);
        }

        return externalModules;
    }
}
