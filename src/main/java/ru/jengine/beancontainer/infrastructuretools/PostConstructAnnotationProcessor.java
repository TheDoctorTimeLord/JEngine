package ru.jengine.beancontainer.infrastructuretools;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;

import java.lang.reflect.Method;
import java.util.Set;

@Bean(isInfrastructure = true)
public class PostConstructAnnotationProcessor implements BeanProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PostConstructAnnotationProcessor.class);

    @Override
    public void preConstructProcess(BeanDefinition beanDefinition) { }

    @Override
    public Object constructProcess(BeanData bean) {
        return bean;
    }

    @Override
    public void postConstructProcess(BeanData bean, BeanFactory beanFactory) {
        Set<Method> postConstructMethods = ReflectionUtils.getAllMethods(
                bean.getBeanBaseClass(),
                method -> method.isAnnotationPresent(PostConstruct.class)
        );

        for (Method postConstructMethod : postConstructMethods) {
            if (!Void.TYPE.equals(postConstructMethod.getReturnType())) {
                LOG.error("PostConstruct method must has void as return type. Method: " + postConstructMethod);
                continue;
            }

            Object[] arguments = beanFactory.findArguments(postConstructMethod);
            try {
                postConstructMethod.setAccessible(true);
                postConstructMethod.invoke(bean.getBeanValue(), arguments);
            } catch (Exception e) {
                LOG.error("Exception during call PostConstruct method: " + postConstructMethod, e);
            }
        }
    }
}
