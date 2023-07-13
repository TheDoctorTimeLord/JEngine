package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.beandefinitionreades.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer2.beandefinitionreades.ImportingBeanDefinitionReader;

public abstract class AnnotationInfrastructureModule extends AnnotationModuleBase {
    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ImportingBeanDefinitionReader(context.moduleClass()));
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.classFinder(), true));
    }
}
