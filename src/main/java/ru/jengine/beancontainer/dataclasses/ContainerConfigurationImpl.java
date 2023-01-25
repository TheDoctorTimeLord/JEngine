package ru.jengine.beancontainer.dataclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import ru.jengine.beancontainer.ClassFinder;
import ru.jengine.beancontainer.implementation.classfinders.ClassPathScanner;

public class ContainerConfigurationImpl implements ContainerConfiguration, ContainerConfigurationBuilder {
    private final Class<?> mainModuleClass;
    private List<Object> additionalBeans = new ArrayList<>();
    private Supplier<ClassFinder> classFinderConsumer = ClassPathScanner::new;

    ContainerConfigurationImpl(Class<?> mainModuleClass) {
        this.mainModuleClass = mainModuleClass;
    }

    @Override
    public List<Object> getAdditionalBeans() {
        return additionalBeans;
    }

    @Override
    public Supplier<ClassFinder> getClassFinderConsumer() {
        return classFinderConsumer;
    }

    @Override
    public Class<?> getMainModuleClass() {
        return mainModuleClass;
    }

    @Override
    public ContainerConfigurationBuilder setAdditionalBeans(List<Object> additionalBeans) {
        this.additionalBeans = additionalBeans;
        return this;
    }

    @Override
    public ContainerConfigurationBuilder addAdditionalBean(Object bean) {
        this.additionalBeans.add(bean);
        return this;
    }

    @Override
    public ContainerConfigurationBuilder setClassFinderConsumer(Supplier<ClassFinder> classFinderConsumer) {
        this.classFinderConsumer = classFinderConsumer;
        return this;
    }

    @Override
    public ContainerConfiguration build() {
        return this;
    }
}
