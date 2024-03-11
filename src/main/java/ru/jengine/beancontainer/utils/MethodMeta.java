package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.exceptions.InvocationMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodMeta {
    private final Method method;
    private final Object owner;

    public MethodMeta(Method method, Object owner) {
        this.method = method;
        this.owner = owner;
    }

    public Object invoke(Object... parameters) {
        try {
            method.setAccessible(true);
            return method.invoke(owner, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InvocationMethodException("Exception during invoke method [" + method + "] for object [" + owner + "]", e);
        }
    }

    public Method getMethod() {
        return method;
    }
}
