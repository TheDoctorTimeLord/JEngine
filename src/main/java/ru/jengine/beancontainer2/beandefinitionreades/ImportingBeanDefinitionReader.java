package ru.jengine.beancontainer2.beandefinitionreades;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.annotations.Import;
import ru.jengine.beancontainer2.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer2.utils.AnnotationUtils;

import java.util.List;
import java.util.stream.Stream;

public class ImportingBeanDefinitionReader implements BeanDefinitionReader {
    private final List<? extends BeanDefinition> importingBeanDefinitions;

    public ImportingBeanDefinitionReader(Class<?> moduleClass) {
        List<Import> importAnnotations = AnnotationUtils.getAnnotations(moduleClass, Import.class);
        this.importingBeanDefinitions = importAnnotations.stream()
                .flatMap(i -> Stream.of(i.value()))
                .map(beanClass -> new JavaClassBeanDefinition(beanClass, Constants.BeanScope.SINGLETON))
                .toList();
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return (List<BeanDefinition>)importingBeanDefinitions;
    }
}
