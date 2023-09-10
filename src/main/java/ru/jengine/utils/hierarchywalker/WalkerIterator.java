package ru.jengine.utils.hierarchywalker;

import ru.jengine.utils.hierarchywalker.WalkingException.Qualification;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.function.Consumer;

public class WalkerIterator implements Iterator<HierarchyElement> {
    private final Deque<WalkingContext> walkingStack = new ArrayDeque<>();
    private final boolean withGenericMapping;
    private final Consumer<HierarchyElement> onGoBackByStarted;

    WalkerIterator(Class<?> startWalkingClass, boolean withTypeChecking, boolean withGenericMapping,
            Class<?>[] initialGenericParameters, Consumer<HierarchyElement> onGoBackByStarted)
    {
        Type[] genericTypes = startWalkingClass.getTypeParameters();

        if (genericTypes.length != initialGenericParameters.length) {
            throw new WalkingException("WalkerIterator for class [%s] must have initial parameters %s, but actual %s"
                    .formatted(startWalkingClass, Arrays.toString(genericTypes), Arrays.toString(initialGenericParameters)));
        }

        this.onGoBackByStarted = onGoBackByStarted;
        this.withGenericMapping = withGenericMapping;
        Map<String, Class<?>> typesMapping =
                prepareTypesMapping(genericTypes, startWalkingClass, withTypeChecking, initialGenericParameters);
        this.walkingStack.addLast(new WalkingContext(startWalkingClass, typesMapping));
    }

    private static Map<String, Class<?>> prepareTypesMapping(Type[] genericTypes, Class<?> startWalkingClass,
            boolean withTypeChecking, Class<?>[] initialGenericParameters)
    {
        if (genericTypes.length == 0) {
            return Map.of();
        }

        if (withTypeChecking) {
            validateCastTypes(startWalkingClass, genericTypes, initialGenericParameters);
        }

        Map<String, Class<?>> result = new HashMap<>();
        for (int i = 0; i < genericTypes.length; i++) {
            result.put(genericTypes[i].getTypeName(), initialGenericParameters[i]);
        }
        return result;
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
        goBackByStartedNodes();

        return currentStep;
    }

    private void markWalkStarted(WalkingContext currentStep) {
        currentStep.markWalkStarted();
        Type[] genericInterfaces = currentStep.getCurrentElement().getGenericInterfaces();
        for (int i = genericInterfaces.length - 1; i >= 0; i--) {
            Type genericInterface = genericInterfaces[i];
            walkingStack.addLast(generateGenericWalkingContext(genericInterface, currentStep));
        }
    }

    private void goBackByStartedNodes() {
        while (!walkingStack.isEmpty()) {
            WalkingContext walkingContext = walkingStack.peekLast();

            if (!walkingContext.isStartWalking()) {
                break;
            }

            onGoBackByStarted.accept(walkingContext);

            walkingStack.pollLast();
            Class<?> currentStepClass = walkingContext.getCurrentElement();
            if (!currentStepClass.isInterface()) {
                Type superclass = currentStepClass.getGenericSuperclass();
                if (superclass != null) {
                    walkingStack.addLast(generateGenericWalkingContext(superclass, walkingContext));
                }
            }
        }
    }

    private WalkingContext generateGenericWalkingContext(Type genericType, WalkingContext currentStep) {
        if (genericType instanceof ParameterizedType parameterized) {
            return generateContextByMapping(parameterized, currentStep.getGenericTypes());
        }
        if (genericType instanceof Class<?> cls) {
            return new WalkingContext(cls, Map.of());
        }
        throw new WalkingException("Unexpected type: [%s] in class [%s]".formatted(genericType, currentStep.getCurrentElement()));
    }

    private WalkingContext generateContextByMapping(ParameterizedType parameterized, Map<String, Class<?>> genericTypes) {
        Class<?> parameterizedClass = (Class<?>) parameterized.getRawType();
        Type[] actualTypes = parameterized.getActualTypeArguments();
        Type[] typeParameters = parameterizedClass.getTypeParameters();

        if (!withGenericMapping) {
            return new WalkingContext(parameterizedClass, Map.of());
        }

        Map<String, Class<?>> actualMapping = new HashMap<>();
        for (int i = 0; i < parameterized.getActualTypeArguments().length; i++) {
            Type actualType = actualTypes[i];
            try {
                Class<?> actualTypeValue = matchTypeParameter(actualType, genericTypes);
                actualMapping.put(typeParameters[i].getTypeName(), actualTypeValue);
            }
            catch (WalkingException e) {
                switch (e.getExceptionQualification()) {
                case MAPPER_NOT_FOUND ->
                        throw new WalkingException("Type [%s] does not have mapped type for variable [%s]".formatted(parameterized, actualType));
                case UNKNOWN_TYPE ->
                        throw new WalkingException("Unexpected type [%s] in type [%s] by index [%s]".formatted(actualType, parameterized, i));
                default -> actualMapping.put(typeParameters[i].getTypeName(), Object.class);
                }
            }
        }
        return new WalkingContext(parameterizedClass, actualMapping);
    }

    private static Class<?> matchTypeParameter(Type actualType, Map<String, Class<?>> actualMatchingTypes)
    {
        if (actualType instanceof Class<?> cls) {
            return cls;
        }
        if (actualType instanceof TypeVariable<?> variable) {
            Class<?> mappedClass = actualMatchingTypes.get(variable.getName());
            if (mappedClass == null) {
                throw new WalkingException(Qualification.MAPPER_NOT_FOUND);
            }
            return mappedClass;
        }
        throw new WalkingException(Qualification.UNKNOWN_TYPE);
    }
}
