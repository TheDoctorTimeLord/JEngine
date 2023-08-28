package ru.jengine.beancontainer2.intstructure.pac5;

import ru.jengine.beancontainer2.annotations.Bean;
import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.extentions.BeanProcessor;

import java.lang.reflect.Field;

@Bean(isInfrastructure = true)
public class DBeanProcessor implements BeanProcessor {
    @Override
    public void preConstructProcess(BeanDefinition beanDefinition, ContainerContext context) { }

    @Override
    public Object constructProcess(Object bean, Class<?> beanClass, ContainerContext context) {
        if (D.class.equals(beanClass)) {
            setTestedField(bean, beanClass, "test1");
        }

        return bean;
    }

    @Override
    public void postConstructProcess(Object bean, Class<?> beanClass, ContainerContext context) {
        if (D.class.equals(beanClass)) {
            setTestedField(bean, beanClass, "test2");
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
