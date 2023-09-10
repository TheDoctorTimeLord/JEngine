package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvingProperties;
import ru.jengine.beancontainer.containercontext.ResolvingPropertyDefinition;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DefaultBeanFactory implements BeanFactory {
    private final BeanExtractor beanExtractor;

    public DefaultBeanFactory(BeanExtractor beanExtractor) {
        this.beanExtractor = beanExtractor;
    }

    @Override
    public Object buildBean(Class<?> beanClass) {
        try {
            Constructor<?> availableConstructor = findAppropriateConstructor(beanClass);
            Object[] args = findArgs(availableConstructor);
            Object builtBean = ReflectionContainerUtils.createObject(availableConstructor, args);
            return autowire(builtBean);
        } catch (Exception ex) {
            throw new ContainerException("Exception while creating bean [" + beanClass + "]", ex);
        }
    }

    @Override
    public Object autowire(Object autowiredBean) {
        return autowiredBean;
    }

    private static Constructor<?> findAppropriateConstructor(Class<?> cls) {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();

        if (constructors.length > 1) {
            constructors = Arrays.stream(constructors)
                    .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                    .toArray(Constructor[]::new);

            if (constructors.length > 1) {
                throw new ContainerException("[%s] has more then 1 constructor annotated Inject".formatted(cls));
            }
        }
        if (constructors.length == 1) {
            Constructor<?> constructor = constructors[0];
            constructor.setAccessible(true);
            return constructor;
        }
        throw new ContainerException("[%s] has no available constructor".formatted(cls));
    }

    private Object[] findArgs(Executable parameterOwner) {
        Object[] result = new Object[parameterOwner.getParameterTypes().length];
        Annotation[][] parametersAnnotations = parameterOwner.getParameterAnnotations();
        for (int i = 0; i < result.length; i++) {
            MethodParameter methodParameter = new MethodParameter(parameterOwner, i, parametersAnnotations[i]);
            result[i] = resolve(methodParameter);
        }
        return result;
    }

    private Object resolve(MethodParameter parameter) {
        Class<?> resultType = castToClass(parameter.getParameterType());
        ResolvingPropertyDefinition properties;

        if (ReflectionContainerUtils.isAvailableCollection(resultType)) {
            properties = ResolvingProperties
                    .properties(getCollectionGenericType(parameter.getGenericParameterType()))
                    .collectionClass(resultType);
        }
        else {
            properties = ResolvingProperties.properties(resultType);
        }

        return resolve(parameter, properties);
    }

    protected Object resolve(MethodParameter parameter, ResolvingPropertyDefinition properties) {
        properties.annotated(parameter.getParameterAnnotations());
        Object extractedBean = getBeanExtractor().getBean(properties);
        return BeanExtractor.isResolved(extractedBean) ? extractedBean : null;
    }

    private static Class<?> getCollectionGenericType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length != 1) {
                throw new ContainerException("Unknown collection type [%s]".formatted(type));
            }
            return castToClass(actualTypeArguments[0]);
        }
        throw new ContainerException("Element [%s] type is not generic".formatted(type));
    }

    private static Class<?> castToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>)type;
        } else if (type instanceof ParameterizedType) {
            return castToClass(((ParameterizedType) type).getRawType());
        }
        throw new ContainerException("Type [" + type + "] is not Class or ParameterizedType");
    }

    protected BeanExtractor getBeanExtractor() {
        return beanExtractor;
    }
}
