package ru.jengine.beancontainer.implementation.moduleimpls;

import ru.jengine.beancontainer.dataclasses.ModuleContext;
import ru.jengine.beancontainer.implementation.beandefinitionreaders.ClassPathBeanDefinitionReader;

public abstract class AnnotationModule extends AnnotationModuleBase {
    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.getClassFinder()));
    }
}
