package ru.jengine.beancontainer.dataclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.implementation.classfinders.ClassPathScanner;

public class ContainerConfiguration {
    private final Class<?> mainModuleClass;
    private List<Object> additionalBeans = new ArrayList<>();
    private Supplier<ClassFinder> classFinderConsumer = ClassPathScanner::new;

    public ContainerConfiguration(Class<?> mainModuleClass) {
        this.mainModuleClass = mainModuleClass;
    }

    public List<Object> getAdditionalBeans() {
        return additionalBeans;
    }

    public Supplier<ClassFinder> getClassFinderConsumer() {
        return classFinderConsumer;
    }

    public Class<?> getMainModuleClass() {
        return mainModuleClass;
    }

    public ContainerConfiguration setAdditionalBeans(List<Object> additionalBeans) {
        this.additionalBeans = additionalBeans;
        return this;
    }

    public ContainerConfiguration addAdditionalBean(Object bean) {
        this.additionalBeans.add(bean);
        return this;
    }

    public ContainerConfiguration setClassFinderConsumer(Supplier<ClassFinder> classFinderConsumer) {
        this.classFinderConsumer = classFinderConsumer;
        return this;
    }

    public static ContainerConfiguration build(Class<?> mainModule) {
        return new ContainerConfiguration(mainModule);
    }
}
