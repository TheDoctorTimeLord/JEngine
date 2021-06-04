package ru.jengine.beancontainer.implementation.moduleimpls;

import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.implementation.beandefinitionreaders.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer.implementation.beandefinitionreaders.ImportingBeanDefinitionReader;

public abstract class AnnotationModule extends AnnotationModuleBase {
    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ImportingBeanDefinitionReader(context.getModuleClass()));
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.getClassFinder()));
    }
}
