package ru.jengine.utils.hierarchywalker;

import java.lang.reflect.Type;
import java.util.Map;

public class WalkingContext implements HierarchyElement {
    private final Class<?> currentClass;
    private final Map<String, Class<?>> genericTypes;
    private boolean startWalking = false;

    public WalkingContext(Class<?> currentClass, Map<String, Class<?>> genericTypes) {
        this.currentClass = currentClass;
        this.genericTypes = genericTypes;
    }

    public void markWalkStarted() {
            startWalking = true;
        }

    public boolean isStartWalking() {
        return startWalking;
    }

    public Map<String, Class<?>> getGenericTypes() {
        return genericTypes;
    }

    @Override
    public Class<?> getCurrentElement() {
        return currentClass;
    }

    @Override
    public Class<?>[] getElementTypeParameters() {
        Type[] typeParameters = currentClass.getTypeParameters();
        Class<?>[] parameters = new Class[typeParameters.length];
        for (int i = 0; i < typeParameters.length; i++) {
            String typeName = typeParameters[i].getTypeName();
            parameters[i] = genericTypes.get(typeName);
        }

        return parameters;
    }

    @Override
    public String toString() {
        return "{" +
                "\"currentClass\"=\"" + currentClass +
                "\", \"genericTypes\"=" + genericTypes +
                "\", \"startWalking\"=" + startWalking +
                "\"}";
    }
}