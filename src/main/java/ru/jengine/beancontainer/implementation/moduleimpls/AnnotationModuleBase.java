package ru.jengine.beancontainer.implementation.moduleimpls;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Context;
import ru.jengine.beancontainer.annotations.ImportModule;
import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.service.Constants;
import ru.jengine.beancontainer.utils.AnnotationUtils;

public abstract class AnnotationModuleBase implements Module {
    private ClassFinder classFinder;
    private final List<BeanDefinitionReader> beanDefinitionReaders = new ArrayList<>();
    private Context contextAnnotation;

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return beanDefinitionReaders;
    }

    @Override
    public void configure(ModuleContext context) {
        this.classFinder = context.getClassFinder();
        this.contextAnnotation = Objects.requireNonNull(getModuleAnnotation(Context.class));

        beanDefinitionReadersInit(context);
    }

    protected abstract void beanDefinitionReadersInit(ModuleContext context);

    protected void addBeanDefinitionReader(BeanDefinitionReader reader) {
        beanDefinitionReaders.add(reader);
    }

    @Override
    public String getContextName() {
        return contextAnnotation == null ? Constants.DEFAULT_CONTEXT : contextAnnotation.value();
    }

    @Override
    public List<String> getBeanSources() {
        return contextAnnotation == null ? Collections.emptyList() : Arrays.asList(contextAnnotation.beanSources());
    }

    @Override
    public boolean needLoadOnContainerInitialize() {
        return contextAnnotation != null && contextAnnotation.needLoadOnContextInitialize();
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
        ImportModule annotation = getModuleAnnotation(ImportModule.class);
        return annotation == null ? Stream.empty() : Arrays.stream(annotation.value());
    }

    @Override
    public List<Class<?>> getImplementations(Class<?> interfaceCls) {
        return new ArrayList<>(classFinder.getSubclasses(interfaceCls));
    }

    @Nullable
    private <A extends Annotation> A getModuleAnnotation(Class<A> contextClass) {
        return AnnotationUtils.getAnnotationSafe(getClass(), contextClass);
    }
}
