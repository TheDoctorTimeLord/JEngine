package ru.jengine.beancontainer.implementation.infrastructure;

import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jengine.beancontainer.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.exceptions.ContainerException;

@Bean(isInfrastructure = true)
public class PreDestroyHandler implements BeanPreRemoveProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PreDestroyHandler.class);

    @Override
    public void preRemoveProcess(BeanContext bean, ContainerContext context) {
        for (Method method : ReflectionUtils.getAllMethods(bean.getBeanClass(), method -> method.isAnnotationPresent(PreDestroy.class))) {
            if (method.getParameterTypes().length != 0) {
                LOG.error("Arguments is not 0", new ContainerException("Method [%s] has any arguments".formatted(method)));
                continue;
            }

            new MethodMeta(method, bean.getBean()).invokeWithInnerParameters();
        }
    }
}
