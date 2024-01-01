package ru.jengine.beancontainer.infrastructuretools;

import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jengine.beancontainer.annotations.AfterInitialize;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessorAdapter;

@Bean(isInfrastructure = true)
public class AfterInitializationAnnotationProcessor extends BeanProcessorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(AfterInitializationAnnotationProcessor.class);

    @Override
    public void afterInitialize(BeanData bean)
    {
        Set<Method> afterInitializeMethods = ReflectionUtils.getAllMethods(
                bean.getBeanBaseClass(),
                method -> method.isAnnotationPresent(AfterInitialize.class)
        );

        for (Method afterInitializeMethod : afterInitializeMethods) {
            if (!Void.TYPE.equals(afterInitializeMethod.getReturnType())) {
                LOG.error("AfterInitialize method must has void as return type. Method: " + afterInitializeMethod);
                continue;
            }
            if (afterInitializeMethod.getParameters().length != 0)
            {
                LOG.error("AfterInitialize method must has no parameters. Method: " + afterInitializeMethod);
                continue;
            }

            try {
                afterInitializeMethod.setAccessible(true);
                afterInitializeMethod.invoke(bean.getBeanValue());
            } catch (Exception e) {
                LOG.error("Exception during call PostConstruct method: " + afterInitializeMethod, e);
            }
        }
    }
}
