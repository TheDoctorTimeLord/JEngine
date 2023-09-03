package ru.jengine.beancontainer.utils;

import ru.jengine.beancontainer.exceptions.ContainerException;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionContainerUtils {
    public static final List<Class<?>> AVAILABLE_COLLECTIONS = List.of(Collection.class, List.class, Set.class);

    public static Object createObjectWithDefaultConstructor(Class<?> cls) {
        try {
            Constructor<?> defaultConstructor = cls.getDeclaredConstructor();
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

    public static boolean isAvailableCollection(@Nullable Class<?> checkedClass) {
        if (checkedClass == null) {
            return false;
        }
        return AVAILABLE_COLLECTIONS.stream().anyMatch(cls -> cls.isAssignableFrom(checkedClass));
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
            throw new ru.jengine.beancontainer.exceptions.ContainerException("Collection with bean must be List or Set but was [" + collectionClass + "]");
        }
    }
}
