package ru.jengine.beancontainer2.beandefinitionreades;

import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.classfinders.ClassFinder;
import ru.jengine.beancontainer2.annotations.Bean;
import ru.jengine.beancontainer2.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer2.utils.AnnotationUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
                .map(entry -> new JavaClassBeanDefinition(entry.getKey(), entry.getValue().scopeName()))
                .collect(Collectors.toList());
    }
}
