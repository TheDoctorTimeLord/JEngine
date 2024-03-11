package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.exceptions.ContainerException;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionContainerUtils {
    private static final List<Class<?>> AVAILABLE_COLLECTIONS = List.of(Collection.class, List.class, Set.class);

    public static Object createObjectWithDefaultConstructor(Class<?> cls) {
        try {
            Constructor<?> defaultConstructor = cls.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ContainerException("[" + cls + "] has not constructor without parameters", e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ContainerException("There was a problem creating the object [" + cls + "]", e);
        }
    }

    public static <T> T createComponentWithDefaultConstructor(Class<?> cls) {
        try {
            return (T) createObjectWithDefaultConstructor(cls);
        } catch (ClassCastException e) {
            throw new ContainerException("[" + cls + "] can not be cast", e);
        }
    }

    public static Object createObject(Constructor<?> constructor, Object[] args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ContainerException("Exception during create object [" + constructor.getDeclaringClass() + "]", e);
        }
    }

    public static String getAvailableCollectionAsString() {
        return AVAILABLE_COLLECTIONS.stream()
                .map(Class::getName)
                .collect(Collectors.joining(", "));
    }

    public static List<Class<?>> getAvailableCollections() {
        return AVAILABLE_COLLECTIONS;
    }

    public static boolean isAvailableCollection(@Nullable Class<?> checkedClass) {
        if (checkedClass == null) {
            return false;
        }
        return AVAILABLE_COLLECTIONS.stream().anyMatch(cls -> cls.isAssignableFrom(checkedClass));
    }

    public static Collection<Object> createCollection(Class<?> collectionClass) {
        if (List.class.isAssignableFrom(collectionClass) || Collection.class.equals(collectionClass)) {
            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(collectionClass)) {
            return new HashSet<>();
        } else {
            throw new ContainerException("Collection with bean must be List, Set or another Collection but was [" + collectionClass + "]");
        }
    }

    /**
     * Конвертирует объект в {@link List список} или {@link Set множество}. Если объект - null, то возвращает пустую
     * коллекцию. Если объект был коллекцией, то преобразовывает все его элементы в новую коллекцию
     *
     * @param object конвертируемый объект
     * @param collectionClass класс коллекции, к которой нужно выполнить конвертирование
     * @return объект новой коллекции
     */
    public static Object convertToCollection(@Nullable Object object, Class<?> collectionClass) {
        Collection<?> beanInst;
        if (object instanceof Collection) {
            beanInst = (Collection<?>) object;
        } else if (object == null) {
            beanInst = Collections.emptyList();
        } else  {
            beanInst = Collections.singletonList(object);
        }

        if (List.class.isAssignableFrom(collectionClass) || Collection.class.isAssignableFrom(collectionClass)) {
            return new ArrayList<>(beanInst);
        } else if (Set.class.isAssignableFrom(collectionClass)) {
            return new HashSet<>(beanInst);
        } else {
            throw new ContainerException("Collection with bean must be List or Set but was [" + collectionClass + "]");
        }
    }

    public static Class<?> getCollectionGenericType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length != 1) {
                throw new ContainerException("Unknown collection type [%s]".formatted(type));
            }
            return castToClass(actualTypeArguments[0]);
        }
        throw new ContainerException("Element [%s] type is not generic".formatted(type));
    }

    public static Class<?> castToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>)type;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return castToClass(parameterizedType.getRawType());
        }
        throw new ContainerException("Type [" + type + "] is not Class or ParameterizedType");
    }
}
