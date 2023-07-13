package ru.jengine.beancontainer2.beandefinitionreades;

import ru.jengine.beancontainer2.BeanDefinition;
import ru.jengine.beancontainer2.BeanDefinitionReader;
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
                .map(JavaClassBeanDefinition::new)
                .toList();
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        return (List<BeanDefinition>)importingBeanDefinitions;
    }
}
