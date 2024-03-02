package ru.jengine.beancontainer.beandefinitionreades;

import org.reflections.ReflectionUtils;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition.BeanProducer;
import ru.jengine.beancontainer.beandefinitions.JavaClassBeanDefinition;
import ru.jengine.beancontainer.modules.Module;
import ru.jengine.beancontainer.utils.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class MethodBeanDefinitionReader implements BeanDefinitionReader {
    private final Module module;

    public MethodBeanDefinitionReader(Module module) {
        this.module = module;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        Set<Method> methods = ReflectionUtils.getAllMethods(module.getClass(), method -> AnnotationUtils.isAnnotationPresent(method, Bean.class));
        return methods.stream()
                .map(method -> (BeanDefinition)new JavaClassBeanDefinition(
                        method.getReturnType(),
                        AnnotationUtils.getAnnotation(method, Bean.class).scopeName(),
                        new BeanProducer(method, module)
                ))
                .toList();
    }
}
