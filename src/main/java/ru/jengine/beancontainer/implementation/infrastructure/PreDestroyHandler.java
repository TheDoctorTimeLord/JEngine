package ru.jengine.beancontainer.implementation.infrastructure;

import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;

import ru.jengine.beancontainer.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;

@Bean(isInfrastructure = true)
public class PreDestroyHandler implements BeanPreRemoveProcessor {
    @Override
    public void preRemoveProcess(BeanContext bean, ContainerContext context) {
        for (Method method : ReflectionUtils.getAllMethods(bean.getBeanClass(), method -> method.isAnnotationPresent(PreDestroy.class))) {
            if (method.getParameterTypes().length != 0) {
                continue; //TODO логировать наличие лишних параметров или бросать ошибку (подумать о её обработке)
            }

            new MethodMeta(method, bean.getBean()).invokeWithInnerParameters();
        }
    }
}
