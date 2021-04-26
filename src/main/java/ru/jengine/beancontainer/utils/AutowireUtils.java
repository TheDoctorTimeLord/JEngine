package ru.jengine.beancontainer.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.implementation.factories.MethodParameter;

public class AutowireUtils {
    public static Object autowire(MethodParameter parameter, ContainerContext context) {
        Class<?> resultType = castToClass(parameter.getParameterType());
        Class<?> collectionClass = null;

        if (Collection.class.isAssignableFrom(resultType)) {
            collectionClass = resultType;
            resultType = getFirstGenericType(parameter.getGenericParameterType());
        }

        BeanContext beanContext = context.getBean(resultType);

        if (collectionClass != null) {
            return  convertCollection(beanContext.getBean(), collectionClass);
        }
        return beanContext.getBean();
    }

    private static Object convertCollection(Object bean, Class<?> collectionClass) {
        Collection<?> beanInst;
        if (bean instanceof Collection) {
            beanInst = (Collection<?>) bean;
        } else {
            beanInst = Collections.singletonList(bean);
        }

        if (List.class.isAssignableFrom(collectionClass)) {
            return new ArrayList<>(beanInst);
        } else if (Set.class.isAssignableFrom(collectionClass)) {
            return new HashSet<>(beanInst);
        } else {
            throw new ContainerException("Collection with bean must be List or Set and is not [" + collectionClass + "]");
        }
    }

    private static Class<?> getFirstGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            return castToClass(((ParameterizedType) type).getActualTypeArguments()[0]);
        }
        throw new ContainerException("Element [" + type + "] type is not generic");
    }

    private static Class<?> castToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>)type;
        } else if (type instanceof ParameterizedType) {
            return castToClass(((ParameterizedType) type).getRawType());
        }
        throw new ContainerException("Type [" + type + "] is not Class or ParameterizedType");
    }
}
