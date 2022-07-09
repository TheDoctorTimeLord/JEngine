package ru.jengine.beancontainer.implementation.contexts.patterns;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContextPattern;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.implementation.contexts.DefaultContainerContext;

public class ModuleBasedContextPattern implements ContextPattern {
    private final List<Module> modules;
    private boolean wasLoaded;

    public ModuleBasedContextPattern(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public ContainerContext buildContext(BeanFactory factory) {
        ContainerContext context = new DefaultContainerContext();
        context.initialize(modules, factory);
        return context;
    }

    @Override
    public List<String> getBeanSources() {
        return modules.stream()
                .map(Module::getBeanSources)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return modules.stream().anyMatch(Module::needLoadOnContainerInitialize);
    }

    @Override
    public boolean wasLoaded() {
        return wasLoaded;
    }

    @Override
    public void setLoaded(boolean isLoaded) {
        this.wasLoaded = isLoaded;
    }

    @Override
    public ContextPattern cloneWithContext(String newContextName) {
        List<Module> modulesInNewContextPattern = modules.stream()
                .map(module -> module.cloneWithContext(newContextName))
                .toList();
        return new ModuleBasedContextPattern(modulesInNewContextPattern);
    }
}
