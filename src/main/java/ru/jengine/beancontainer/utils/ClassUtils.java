package ru.jengine.beancontainer.utils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import ru.jengine.beancontainer.exceptions.UtilsException;

public class ClassUtils {
    public static final Predicate<Class<?>> IS_CLASS_PREDICATE = cls -> !cls.isAnnotation() && !cls.isInterface();

    public static Class<?> getFirstGenericType(AnnotatedElement element)
    {
        Type type = null;

        if (element instanceof Field)
        {
            type = ((Field)element).getGenericType();
        }
        else if (element instanceof Method)
        {
            type = ((Method)element).getGenericReturnType();
        }

        if (type instanceof ParameterizedType)
        {
            ParameterizedType pType = (ParameterizedType)type;
            return (Class<?>)pType.getActualTypeArguments()[0];
        }
        else
        {
            throw new UtilsException("Element [" + element + "] type is not generic");
        }
    }
}
