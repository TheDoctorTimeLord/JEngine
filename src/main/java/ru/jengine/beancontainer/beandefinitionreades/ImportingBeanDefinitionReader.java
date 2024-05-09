package ru.jengine.beancontainer.beandefinitionreades;

import ru.jengine.beancontainer.Constants.BeanScope;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.annotations.Shared;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.utils.AnnotationUtils;

import java.util.List;
import java.util.stream.Stream;

public class ImportingBeanDefinitionReader implements BeanDefinitionReader {
    private final List<BeanDefinition> importingBeanDefinitions;

    public ImportingBeanDefinitionReader(Class<?> moduleClass) {
        List<Import> importAnnotations = AnnotationUtils.getAnnotations(moduleClass, Import.class);
        this.importingBeanDefinitions = importAnnotations.stream()
                .flatMap(i -> Stream.of(i.value()))
                .map(beanClass -> (BeanDefinition)new JavaClassBeanDefinition(
                        beanClass,
                        BeanScope.SINGLETON,
                        AnnotationUtils.isAnnotationPresent(beanClass, Shared.class)
                ))
                .toList();
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return importingBeanDefinitions;
    }
}
