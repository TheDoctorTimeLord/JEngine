package ru.jengine.beancontainer2.beanfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;

public class MethodParameter {
    private final Executable parameterOwner;
    private final int parameterPosition;
    private final Annotation[] parameterAnnotations;

    public MethodParameter(Executable parameterOwner, int parameterPosition, Annotation[] parameterAnnotations) {
        this.parameterOwner = parameterOwner;
        this.parameterPosition = parameterPosition;
        this.parameterAnnotations = parameterAnnotations;
    }

    public Executable getParameterOwner() {
        return parameterOwner;
    }

    public int getParameterPosition() {
        return parameterPosition;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public Type getParameterType() {
        return this.parameterOwner.getParameterTypes()[getParameterPosition()];
    }

    public boolean isParameterAnnotated(Class<? extends Annotation> annotation) {
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if (parameterAnnotation.annotationType().equals(annotation)) {
                return true;
            }
        }
        return false;
    }
}
