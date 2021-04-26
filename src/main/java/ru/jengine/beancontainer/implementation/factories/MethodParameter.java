package ru.jengine.beancontainer.implementation.factories;

import java.lang.reflect.Executable;
import java.lang.reflect.Type;

import ru.jengine.beancontainer.exceptions.ContainerException;

public class MethodParameter {
    private final Executable executable;
    private final int parameterIndex;

    public MethodParameter(Executable executable, int parameterIndex) {
        if (parameterIndex < 0) {
            throw new ContainerException("Parameter index for [" + executable + "] less than 0");
        }

        this.executable = executable;
        this.parameterIndex = parameterIndex;
    }

    public Type getParameterType() {
        return this.executable.getParameterTypes()[this.parameterIndex];
    }

    public Type getGenericParameterType() {
        Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
        return parameterIndex >= 0 && parameterIndex < genericParameterTypes.length
                ? genericParameterTypes[parameterIndex]
                : getParameterType();
    }
}
