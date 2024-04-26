package ru.jengine.beancontainer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;

public class ParametersContainer {
    private final Class<?>[] parameterTypes;
    private final Type[] genericParameterTypes;
    private final Annotation[][] parametersAnnotations;
    private final Annotation[] containerAnnotations;

    public ParametersContainer(Executable executable) {
        this(executable.getParameterTypes(), executable.getGenericParameterTypes(), executable.getParameterAnnotations(), executable.getAnnotations());
    }

    public ParametersContainer(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] parametersAnnotations, Annotation[] containerAnnotations) {
        this.parameterTypes = parameterTypes;
        this.genericParameterTypes = genericParameterTypes;
        this.parametersAnnotations = parametersAnnotations;
        this.containerAnnotations = containerAnnotations;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Type[] getGenericParameterTypes() {
        return genericParameterTypes;
    }

    public Annotation[][] getParametersAnnotations() {
        return parametersAnnotations;
    }

    public Annotation[] getContainerAnnotations() {
        return containerAnnotations;
    }
}
