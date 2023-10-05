package ru.jengine.beancontainer.intstructure.pac5;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.BeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;

import java.lang.reflect.Field;

@Bean(isInfrastructure = true)
public class DBeanProcessor implements BeanProcessor {
    @Override
    public void preConstructProcess(BeanDefinition beanDefinition) { }

    @Override
    public Object constructProcess(BeanData bean) {
        if (D.class.equals(bean.getBeanBaseClass())) {
            setTestedField(bean.getBeanValue(), bean.getBeanBaseClass(), "test1");
        }

        return bean;
    }

    @Override
    public void postConstructProcess(BeanData bean, BeanFactory beanFactory) {
        if (D.class.equals(bean.getBeanBaseClass())) {
            setTestedField(bean.getBeanValue(), bean.getBeanBaseClass(), "test2");
        }
    }

    private void setTestedField(Object bean, Class<?> beanClass, String fieldName) {
        try {
            Field field = beanClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
