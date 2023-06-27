package ru.jengine.utils.hierarchywalker;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class WalkerIterator implements Iterator<HierarchyElement> {
    private final Deque<WalkingContext> walkingStack = new ArrayDeque<>();

    public WalkerIterator(Class<?> startWalkingClass) {
        walkingStack.addLast(new WalkingContext(startWalkingClass, Map.of()));
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
