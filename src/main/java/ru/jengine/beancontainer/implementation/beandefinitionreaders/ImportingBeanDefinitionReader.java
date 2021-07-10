package ru.jengine.beancontainer.implementation.beandefinitionreaders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.implementation.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.implementation.factories.SingletonBeanFactoryStrategy;
import ru.jengine.beancontainer.utils.AnnotationUtils;

public class ImportingBeanDefinitionReader implements BeanDefinitionReader {
    private final List<Class<?>> importingClasses;

    public ImportingBeanDefinitionReader(Class<?> moduleClass) {
        List<Import> importAnnotations = AnnotationUtils.getAnnotations(moduleClass, Import.class);
        this.importingClasses = importAnnotations.stream()
                .flatMap(i -> Stream.of(i.value()))
                .collect(Collectors.toList());;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return importingClasses.stream()
                .map(cls -> new JavaClassBeanDefinition(cls, new SingletonBeanFactoryStrategy(cls)))
                .collect(Collectors.toList());
    }
}
