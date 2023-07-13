package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.beandefinitionreades.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer.beandefinitionreades.ImportingBeanDefinitionReader;

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
