package ru.jengine.beancontainer.beanfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class Parameter {
    private final ParametersContainer parametersContainer;
    private final int parameterPosition;

    public Parameter(ParametersContainer parametersContainer, int parameterPosition)
    {
        this.parametersContainer = parametersContainer;
        this.parameterPosition = parameterPosition;
    }

    public int getParameterPosition() {
        return parameterPosition;
    }

    public Annotation[] getParameterAnnotations() {
        return parametersContainer.getAnnotations()[getParameterPosition()];
    }

    public Type getParameterType() {
        return parametersContainer.getParameterTypes()[getParameterPosition()];
    }

    public Type getGenericParameterType() {
        return parametersContainer.getGenericParameterTypes()[getParameterPosition()];
    }

    public boolean isParameterAnnotated(Class<? extends Annotation> annotation) {
        for (Annotation parameterAnnotation : getParameterAnnotations()) {
            if (parameterAnnotation.annotationType().equals(annotation)) {
                return true;
            }
        }
        return false;
    }
}
