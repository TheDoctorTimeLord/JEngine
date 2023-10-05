package ru.jengine.beancontainer.infrastructuretools;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PreDestroy;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanPreRemoveProcessor;

import java.lang.reflect.Method;
import java.util.Set;

@Bean(isInfrastructure = true)
public class PreDestroyAnnotationProcessor implements BeanPreRemoveProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PreDestroyAnnotationProcessor.class);

    @Override
    public void preRemoveProcess(BeanData bean) {
        Set<Method> preDestroyMethods = ReflectionUtils.getAllMethods(
                bean.getBeanBaseClass(),
                method -> method.isAnnotationPresent(PreDestroy.class)
        );

        for (Method preDestroyMethod : preDestroyMethods) {
            if (!Void.TYPE.equals(preDestroyMethod.getReturnType())) {
                LOG.error("PreDestroy method must has void as return type. Method: " + preDestroyMethod);
                continue;
            }
            if (preDestroyMethod.getParameterTypes().length != 0) {
                LOG.error("PreDestroy method must doesn't have parameters. Method: " + preDestroyMethod);
                continue;
            }
            try {
                preDestroyMethod.setAccessible(true);
                preDestroyMethod.invoke(bean.getBeanValue());
            } catch (Exception e) {
                LOG.error("Exception during call PreDestroy method: " + preDestroyMethod, e);
            }
        }
    }
}
