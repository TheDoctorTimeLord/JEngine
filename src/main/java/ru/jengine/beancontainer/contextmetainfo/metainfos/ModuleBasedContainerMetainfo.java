package ru.jengine.beancontainer.contextmetainfo.metainfos;

import java.util.ArrayList;
import java.util.List;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfo;
import ru.jengine.beancontainer.modules.Module;

public class ModuleBasedContainerMetainfo implements ContextMetainfo {
    private final List<Module> modules;

    public ModuleBasedContainerMetainfo(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public List<BeanDefinition> extractBeanDefinitions() {
        return modules.stream()
                .flatMap(module -> module.getBeanDefinitionReaders().stream())
                .flatMap(reader -> reader.readBeanDefinitions().stream())
                .toList();
    }

    @Override
    public List<String> getBeanSources() {
        return modules.stream()
                .flatMap(module -> module.getBeanSources().stream())
                .distinct()
                .toList();
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return modules.stream().anyMatch(Module::needLoadOnContainerInitialize);
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }
}
