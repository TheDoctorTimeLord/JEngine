package ru.jengine.utils.hierarchywalker;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class WalkerIterator implements Iterator<HierarchyElement> {
    private final Deque<WalkingContext> walkingStack = new ArrayDeque<>();

    public WalkerIterator(Class<?> startWalkingClass) {
        this(startWalkingClass, false);
    }

    public WalkerIterator(Class<?> startWalkingClass, boolean withTypeChecking, Class<?>... startGenericParameters) {
        Type[] genericTypes = startWalkingClass.getTypeParameters();

        if (genericTypes.length != startGenericParameters.length) {
            throw new WalkingException("WalkerIterator for class [%s] must have initial parameters %s, but actual %s"
                    .formatted(startWalkingClass, Arrays.toString(genericTypes), Arrays.toString(startGenericParameters)));
        }

        Map<String, Class<?>> typesMapping;
        if (genericTypes.length == 0) {
            typesMapping = Map.of();
        }
        else {
            if (withTypeChecking) {
                validateCastTypes(startWalkingClass, genericTypes, startGenericParameters);
            }
            typesMapping = new HashMap<>();
            for (int i = 0; i < genericTypes.length; i++) {
                typesMapping.put(genericTypes[i].getTypeName(), startGenericParameters[i]);
            }
        }

        walkingStack.addLast(new WalkingContext(startWalkingClass, typesMapping));
    }

    private static void validateCastTypes(Class<?> startWalkingClass, Type[] genericTypes, Class<?>[] startGenericParameters)
    {
        Map<Type, Class<?>> currentMapping = new HashMap<>();
        for (int i = 0; i < genericTypes.length; i++) {
            Type genericType = genericTypes[i];
            currentMapping.put(genericType, startGenericParameters[i]);

            if (!(genericType instanceof TypeVariable<?>)) {
                throw new WalkingException("Class [%s] at position [%s] has [%s] but expected TypeVariable"
                        .formatted(startWalkingClass, i, genericType));
            }
        }

        for (int i = 0; i < genericTypes.length; i++) {
            Type genericType = genericTypes[i];
            Class<?> startGenericParameter = startGenericParameters[i];
            try {
                validateCastTypes(startGenericParameter, new Type[]{genericType}, currentMapping);
            }
            catch (WalkingException e) {
                throw new WalkingException("In type [%s] type %s with position [%s] is not casted to [%s]"
                        .formatted(startWalkingClass, startGenericParameter, i, genericType), e);
            }
        }
    }

    private static void validateCastTypes(Class<?> casted, Type[] castingTo, Map<Type, Class<?>> currentMapping)
    {
        for (Type type : castingTo) {
            Class<?> dependency;
            if (type instanceof TypeVariable<?> typeVariable) {
                dependency = currentMapping.get(typeVariable);
                validateCastTypes(dependency, typeVariable.getBounds(), currentMapping);
            }
            else if (type instanceof Class<?> cls) {
                dependency = cls;
            }
            else {
                throw new WalkingException("Unknown type [%s]".formatted(type));
            }

            if (!dependency.isAssignableFrom(casted)) {
                throw new WalkingException("Type [%s] is not extended [%s]".formatted(casted, dependency));
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !walkingStack.isEmpty();
    }

    @Override
    public HierarchyElement next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Walking is over");
        }

        WalkingContext currentStep = walkingStack.peekLast();

        if (currentStep == null || currentStep.isStartWalking()) {
            throw new WalkingException("Current step can not be [%s] in walking path [%s]".formatted(currentStep, walkingStack));
        }

        markWalkStarted(currentStep);
        goBackByMarked();

        return currentStep;
    }

    private void markWalkStarted(WalkingContext currentStep) {
        currentStep.markWalkStarted();
        Type[] genericInterfaces = currentStep.getCurrentElement().getGenericInterfaces();
        for (int i = genericInterfaces.length - 1; i >= 0; i--) {
            Type genericInterface = genericInterfaces[i];
            addGenericWalkingContext(genericInterface, currentStep);
        }
    }

    private void goBackByMarked() {
        while (!walkingStack.isEmpty()) {
            WalkingContext walkingContext = walkingStack.peekLast();

            if (walkingContext.isStartWalking()) {
                onPollWalkingContext(walkingStack.pollLast());
            }
            else {
                break;
            }
        }
    }

    private void onPollWalkingContext(WalkingContext walkingContext) {
        Class<?> currentStepClass = walkingContext.getCurrentElement();
        if (!currentStepClass.isInterface()) {
            Type superclass = currentStepClass.getGenericSuperclass();
            if (superclass != null) {
                addGenericWalkingContext(superclass, walkingContext);
            }
        }
    }

    private void addGenericWalkingContext(Type genericType, WalkingContext currentStep) {
        if (genericType instanceof ParameterizedType parameterized) {
            walkingStack.addLast(generateContextByMapping(parameterized, currentStep.getGenericTypes()));
        }
        else if (genericType instanceof Class<?> cls) {
            walkingStack.addLast(new WalkingContext(cls, Map.of()));
        }
        else {
            throw new WalkingException("Unexpected type: [%s] in class [%s]".formatted(genericType, currentStep.getCurrentElement()));
        }
    }

    private static WalkingContext generateContextByMapping(ParameterizedType parameterized, Map<String, Class<?>> genericTypes) {
        Class<?> parameterizedClass = (Class<?>) parameterized.getRawType();
        Type[] actualTypes = parameterized.getActualTypeArguments();
        Type[] typeParameters = parameterizedClass.getTypeParameters();
        Map<String, Class<?>> actualMapping = new HashMap<>();
        for (int i = 0; i < parameterized.getActualTypeArguments().length; i++) {
            Type actualType = actualTypes[i];
            String parameterName = typeParameters[i].getTypeName();
            if (actualType instanceof Class<?> cls) {
                actualMapping.put(parameterName, cls);
            }
            else if (actualType instanceof TypeVariable<?> variable) {
                Class<?> mappedClass = genericTypes.get(variable.getName());
                if (mappedClass == null) {
                    throw new WalkingException("Type [%s] does not have mapped type for variable [%s]".formatted(actualTypes, variable));
                }
                actualMapping.put(parameterName, mappedClass);
            }
            else {
                throw new WalkingException("Unexpected type [%s] in type [%s] by index [%s]".formatted(actualType, parameterized, i));
            }
        }
        return new WalkingContext(parameterizedClass, actualMapping);
    }
}
