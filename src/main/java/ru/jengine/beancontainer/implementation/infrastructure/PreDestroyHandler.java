package ru.jengine.beancontainer.implementation.infrastructure;

import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;

import ru.jengine.beancontainer.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.utils.BeanUtils;
import ru.jengine.utils.Logger;

@Bean(isInfrastructure = true)
public class PreDestroyHandler implements BeanPreRemoveProcessor {
    @Override
    public void preRemoveProcess(BeanContext bean, ContainerContext context) {
        for (Method method : ReflectionUtils.getAllMethods(bean.getBeanClass(), method -> method.isAnnotationPresent(PreDestroy.class))) {
            if (method.getParameterTypes().length != 0) {
                Logger logger = BeanUtils.getLogger(context);
                if (logger != null) {
                    logger.error("PreDestroy", new ContainerException("Method [%s] has any arguments".formatted(method)));
                }

                continue;
            }

            new MethodMeta(method, bean.getBean()).invokeWithInnerParameters();
        }
    }
}
