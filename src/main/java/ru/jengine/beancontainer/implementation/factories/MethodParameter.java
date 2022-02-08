package ru.jengine.beancontainer.implementation.factories;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import ru.jengine.beancontainer.exceptions.ContainerException;

public class MethodParameter {
    private final Executable executable;
    private final int parameterIndex;
    private final List<Annotation> parameterAnnotation;

    public MethodParameter(Executable executable, int parameterIndex, Annotation... parameterAnnotation) {
        this.parameterAnnotation = Arrays.asList(parameterAnnotation);
        if (parameterIndex < 0) {
            throw new ContainerException("Parameter index for [" + executable + "] less than 0");
        }

        this.executable = executable;
        this.parameterIndex = parameterIndex;
    }

    public List<Annotation> getParameterAnnotation() {
        return parameterAnnotation;
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

    public boolean isParameterAnnotated(Class<? extends Annotation> annotation) {
        for (Annotation parameterAnnotation : parameterAnnotation) {
            if (parameterAnnotation.annotationType().equals(annotation)) {
                return true;
            }
        }
        return false;
    }
}
