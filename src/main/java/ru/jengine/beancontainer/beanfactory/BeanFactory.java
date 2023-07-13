package ru.jengine.beancontainer.beanfactory;

import java.lang.reflect.Executable;

public interface BeanFactory {
    Object buildBean(Class<?> beanClass);

    Object[] findArguments(Executable parametersOwner);
}
