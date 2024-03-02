package ru.jengine.beancontainer.modules;

import ru.jengine.beancontainer.beandefinitionreades.ClassPathBeanDefinitionReader;
import ru.jengine.beancontainer.beandefinitionreades.ImportingBeanDefinitionReader;
import ru.jengine.beancontainer.beandefinitionreades.MethodBeanDefinitionReader;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class AnnotationModule extends AnnotationModuleBase {
    @Override
    protected void beanDefinitionReadersInit(ModuleContext context) {
        addBeanDefinitionReader(new ImportingBeanDefinitionReader(context.moduleClass()));
        addBeanDefinitionReader(new ClassPathBeanDefinitionReader(context.classFinder()));
        addBeanDefinitionReader(new MethodBeanDefinitionReader(this));
    }

    public Set<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotation) {
        return classFinder.getAnnotatedClasses(annotation);
    }
}
