package ru.jengine.beancontainer.implementation.beandefinitionreaders;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.implementation.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.utils.AnnotationUtils;

public class ClassPathBeanDefinitionReader implements BeanDefinitionReader {
    private final ClassFinder classFinder;
    private final Boolean findInfrastructureBeans;

    public ClassPathBeanDefinitionReader(ClassFinder classFinder) {
        this(classFinder, false);
    }

    public ClassPathBeanDefinitionReader(ClassFinder classFinder, boolean findInfrastructureBeans) {
        this.classFinder = classFinder;
        this.findInfrastructureBeans = findInfrastructureBeans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        Set<Class<?>> beanClasses = classFinder.getAnnotatedClasses(Bean.class);
        return beanClasses.stream()
                .filter(cls -> findInfrastructureBeans.equals(AnnotationUtils.getAnnotation(cls, Bean.class).isInfrastructure()))
                .map(cls -> new JavaClassBeanDefinition(cls, true)) //TODO исправить синглтоны
                .collect(Collectors.toList());
    }
}
