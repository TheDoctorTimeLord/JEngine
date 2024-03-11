package ru.jengine.beancontainer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;

public class ParametersContainer {
    private final Class<?>[] parameterTypes;
    private final Type[] genericParameterTypes;
    private final Annotation[][] annotations;

    public ParametersContainer(Executable executable) {
        this(executable.getParameterTypes(), executable.getGenericParameterTypes(), executable.getParameterAnnotations());
    }

    public ParametersContainer(Class<?>[] parameterTypes, Type[] genericParameterTypes, Annotation[][] annotations) {
        this.parameterTypes = parameterTypes;
        this.genericParameterTypes = genericParameterTypes;
        this.annotations = annotations;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Type[] getGenericParameterTypes() {
        return genericParameterTypes;
    }

    public Annotation[][] getAnnotations() {
        return annotations;
    }
}
