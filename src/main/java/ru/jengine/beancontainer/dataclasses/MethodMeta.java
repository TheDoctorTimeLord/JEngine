package ru.jengine.beancontainer.dataclasses;

import ru.jengine.beancontainer.exceptions.InvocationMethodException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodMeta {
    private final Method method;
    private final Object owner;
    private final Object[] parameters;

    public MethodMeta(Method method, Object owner, Object... parameters) {
        this.method = method;
        this.owner = owner;
        this.parameters = parameters;
    }

    public Object invokeWithInnerParameters() {
        return invoke(parameters);
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

    public Object[] getParameters() {
        return parameters;
    }
}
