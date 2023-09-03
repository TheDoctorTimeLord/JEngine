package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.beandefinitionreades.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer.beandefinitionreades.ImportingBeanDefinitionReader;

public abstract class AnnotationModule extends AnnotationModuleBase {
    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ImportingBeanDefinitionReader(context.moduleClass()));
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.classFinder()));
    }
}
