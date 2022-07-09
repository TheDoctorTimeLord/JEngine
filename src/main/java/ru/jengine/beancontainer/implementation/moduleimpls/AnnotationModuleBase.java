package ru.jengine.beancontainer.implementation.moduleimpls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.ImportModule;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.utils.AnnotationUtils;

public abstract class AnnotationModuleBase implements Module, Cloneable {
    private final List<BeanDefinitionReader> beanDefinitionReaders = new ArrayList<>();
    private final ContainerModule containerModuleAnnotation;
    private ClassFinder classFinder;
    private String contextName;

    public AnnotationModuleBase() {
        this.containerModuleAnnotation = AnnotationUtils.getAnnotationSafe(getClass(), ContainerModule.class);
        this.contextName = containerModuleAnnotation == null
                ? Contexts.DEFAULT_CONTEXT
                : containerModuleAnnotation.contextName();
    }

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return beanDefinitionReaders;
    }

    @Override
    public void configure(ModuleContext context) {
        this.classFinder = context.getClassFinder();
        beanDefinitionReadersInit(context);
    }

    @Override
    public Module cloneWithContext(String newContextName) {
        try {
            AnnotationModuleBase clonedModule = (AnnotationModuleBase)super.clone();
            clonedModule.contextName = newContextName;
            return clonedModule;
        }
        catch (CloneNotSupportedException e) {
            throw new ContainerException("Something went wrong", e);
        }
    }

    protected abstract void beanDefinitionReadersInit(ModuleContext context);

    protected void addBeanDefinitionReader(BeanDefinitionReader reader) {
        beanDefinitionReaders.add(reader);
    }

    @Override
    public String getContextName() {
        return contextName;
    }

    @Override
    public List<String> getBeanSources() {
        return containerModuleAnnotation == null
                ? Collections.emptyList()
                : Arrays.asList(containerModuleAnnotation.beanSources());
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return containerModuleAnnotation != null && containerModuleAnnotation.needLoadOnContextInitialize();
    }

    @Override
    public List<Class<?>> getSubmodules() {
        Stream<Class<?>> submodulesByAnnotation = classFinder.getAnnotatedClasses(ContainerModule.class).stream()
                .filter(cls -> !getClass().equals(cls));

        Stream<Class<?>> submodulesByImports = handleImportModuleAnnotation();

        return Stream.concat(submodulesByAnnotation, submodulesByImports)
                .distinct()
                .collect(Collectors.toList());
    }

    private Stream<Class<?>> handleImportModuleAnnotation() {
        return AnnotationUtils.getAnnotations(getClass(), ImportModule.class).stream()
                .flatMap(annotation -> Arrays.stream(annotation.value()));
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> interfaceCls) {
        return new ArrayList<>(classFinder.getSubclasses(interfaceCls));
    }
}
