package ru.jengine.beancontainer.implementation.moduleimpls;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.dataclasses.ModuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AnnotationModuleBase implements Module {
    private ClassFinder classFinder;
    private final List<BeanDefinitionReader> beanDefinitionReaders = new ArrayList<>();

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return beanDefinitionReaders;
    }

    @Override
    public void configure(ModuleContext context) {
        this.classFinder = context.getClassFinder();
        beanDefinitionReadersInit(context);
    }

    protected abstract void beanDefinitionReadersInit(ModuleContext context);

    protected void addBeanDefinitionReader(BeanDefinitionReader reader) {
        beanDefinitionReaders.add(reader);
    }

    @Override
    public List<Class<?>> getSubmodules() {
        return classFinder.getAnnotatedClasses(ContainerModule.class).stream()
                .filter(cls -> !getClass().equals(cls))
                .collect(Collectors.toList());
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> interfaceCls) {
        return new ArrayList<>(classFinder.getSubclasses(interfaceCls));
    }

}
