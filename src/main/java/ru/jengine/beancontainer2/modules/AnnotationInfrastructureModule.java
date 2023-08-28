package ru.jengine.beancontainer2.modules;

import ru.jengine.beancontainer2.Constants;
import ru.jengine.beancontainer2.beandefinitionreades.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer2.beandefinitionreades.ImportingBeanDefinitionReader;

public abstract class AnnotationInfrastructureModule extends AnnotationModuleBase {
    protected AnnotationInfrastructureModule() {
        setContextName(Constants.Contexts.INFRASTRUCTURE_CONTEXT);
    }

    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ImportingBeanDefinitionReader(context.moduleClass()));
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.classFinder(), true));
    }
}
