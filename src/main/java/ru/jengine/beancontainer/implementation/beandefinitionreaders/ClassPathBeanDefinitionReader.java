package ru.jengine.beancontainer.implementation.beandefinitionreaders;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.BeanFactoryStrategy;
import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.implementation.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.service.Pair;
import ru.jengine.beancontainer.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.BeanUtils;

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
    public List<BeanDefinition> readBeanDefinitions() {
        Set<Class<?>> beanClasses = classFinder.getAnnotatedClasses(Bean.class);
        return beanClasses.stream()
                .map(cls -> new Pair<>(cls, AnnotationUtils.getAnnotation(cls, Bean.class)))
                .filter(pair -> findInfrastructureBeans.equals(pair.getValue().isInfrastructure()))
                .map(pair -> {
                    Class<?> cls = pair.getKey();
                    BeanFactoryStrategy strategy = createStrategy(pair.getValue().strategyCode(), cls);
                    return new JavaClassBeanDefinition(cls, strategy);
                })
                .collect(Collectors.toList());
    }

    protected BeanFactoryStrategy createStrategy(String strategyCode, Class<?> strategyOwner) {
        return BeanUtils.createStrategy(strategyOwner, strategyCode);
    }
}
