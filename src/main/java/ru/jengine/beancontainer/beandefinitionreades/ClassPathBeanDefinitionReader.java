package ru.jengine.beancontainer.beandefinitionreades;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.classfinders.ClassFinder;
import ru.jengine.beancontainer.utils.AnnotationUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassPathBeanDefinitionReader implements BeanDefinitionReader {
    private final ClassFinder classFinder;
    private final boolean findInfrastructureBeans;

    public ClassPathBeanDefinitionReader(ClassFinder classFinder) {
        this(classFinder, false);
    }

    public ClassPathBeanDefinitionReader(ClassFinder classFinder, boolean findInfrastructureBeans) {
        this.classFinder = classFinder;
        this.findInfrastructureBeans = findInfrastructureBeans;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        Set<Class<?>> beanClasses = classFinder.getAnnotatedClasses(Bean.class);
        return beanClasses.stream()
                .map(cls -> Map.entry(cls, AnnotationUtils.getAnnotation(cls, Bean.class)))
                .filter(entry -> findInfrastructureBeans == entry.getValue().isInfrastructure())
                .map(entry -> (BeanDefinition)new JavaClassBeanDefinition(
                        entry.getKey(),
                        entry.getValue().scopeName(),
                        AnnotationUtils.isAnnotationPresent(entry.getKey(), Shared.class)
                ))
                .toList();
    }
}
