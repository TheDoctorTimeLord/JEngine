package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.annotations.Bean;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathBeanDefinitionReader implements BeanDefinitionReader {
    private final ClassFinder classFinder;

    public ClassPathBeanDefinitionReader(ClassFinder classFinder) {
        this.classFinder = classFinder;
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        Set<Class<?>> beanClasses = classFinder.getAnnotatedClasses(Bean.class); //TODO исправить сканирование
        return beanClasses.stream()
                .map(cls -> new JavaClassBeanDefinition(cls, true)) //TODO исправить синглтоны
                .collect(Collectors.toList());
    }
}
