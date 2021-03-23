package ru.jengine.beancontainer.implementation;

import ru.jengine.beancontainer.BeanDefinitionReader;
import ru.jengine.beancontainer.Module;
import ru.jengine.beancontainer.dataclasses.ModuleContext;

import java.util.ArrayList;
import java.util.List;

public class AnnotationModule implements Module {
    private List<BeanDefinitionReader> beanDefinitionReaders = new ArrayList<>();

    @Override
    public List<BeanDefinitionReader> getBeanDefinitionReaders() {
        return beanDefinitionReaders;
    }

    @Override
    public void configure(ModuleContext context) {
        beanDefinitionReaders.add(new ClassPathBeanDefinitionReader(context.getClassFinder()));
    }

}
