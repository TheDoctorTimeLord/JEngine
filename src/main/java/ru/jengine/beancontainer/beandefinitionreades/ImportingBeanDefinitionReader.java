package ru.jengine.beancontainer.beandefinitionreades;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.utils.AnnotationUtils;

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
