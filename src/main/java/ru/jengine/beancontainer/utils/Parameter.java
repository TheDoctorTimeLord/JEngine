package ru.jengine.beancontainer.utils;

import ru.jengine.utils.AnnotationUtils;

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
        return parametersContainer.getParametersAnnotations()[getParameterPosition()];
    }

    public ParametersContainer getContainer() {
        return parametersContainer;
    }

    public Type getParameterType() {
        return parametersContainer.getParameterTypes()[getParameterPosition()];
    }

    public Type getGenericParameterType() {
        return parametersContainer.getGenericParameterTypes()[getParameterPosition()];
    }

    public boolean isParameterAnnotated(Class<? extends Annotation> annotation) {
        return AnnotationUtils.extractAnnotation(getParameterAnnotations(), annotation) != null;
    }
}
