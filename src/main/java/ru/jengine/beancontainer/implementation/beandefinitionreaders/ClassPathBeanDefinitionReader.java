package ru.jengine.beancontainer.implementation.beandefinitionreaders;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.implementation.beandefinitions.JavaClassBeanDefinition;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathBeanDefinitionReader implements BeanDefinitionReader {
    private final ClassFinder classFinder;
    private Boolean findInfrastructureBeans;

    public ClassPathBeanDefinitionReader(ClassFinder classFinder) {
        this(classFinder, false);
    }

    public ClassPathBeanDefinitionReader(ClassFinder classFinder, boolean findInfrastructureBeans) {
        this.classFinder = classFinder;
        this.findInfrastructureBeans = findInfrastructureBeans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        Set<Class<?>> beanClasses = classFinder.getAnnotatedClasses(Bean.class); //TODO исправить сканирование
        return beanClasses.stream()
                .filter(cls -> findInfrastructureBeans.equals(cls.getAnnotation(Bean.class).isInfrastructure()))
                .map(cls -> new JavaClassBeanDefinition(cls, true)) //TODO исправить синглтоны
                .collect(Collectors.toList());
    }
}
