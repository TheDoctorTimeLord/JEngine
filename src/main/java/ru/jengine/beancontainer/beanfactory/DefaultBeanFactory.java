package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.annotations.Inject;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beandefinitions.BeanDefinition.BeanProducer;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingPropertyDefinition;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.utils.Parameter;
import ru.jengine.beancontainer.utils.ParametersContainer;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static ru.jengine.beancontainer.utils.ReflectionContainerUtils.castToClass;
import static ru.jengine.beancontainer.utils.ReflectionContainerUtils.getCollectionGenericType;

public class DefaultBeanFactory implements BeanFactory {
    private final BeanExtractor beanExtractor;

    public DefaultBeanFactory(BeanExtractor beanExtractor) {
        this.beanExtractor = beanExtractor;
    }

    @Override
    public Object buildBean(BeanDefinition definition) {
        try {
            if (definition.getBeanProducer() != null) {
                BeanProducer beanProducer = definition.getBeanProducer();
                Object[] args = findArguments(beanProducer.parametersContainer());
                return beanProducer.producer().apply(args);
            }

            Constructor<?> availableConstructor = findAppropriateConstructor(definition.getBeanClass());
            Object[] args = findArguments(new ParametersContainer(availableConstructor));
            return ReflectionContainerUtils.createObject(availableConstructor, args);
        } catch (Exception ex) {
            throw new ContainerException("Exception while creating bean [" + definition + "]", ex);
        }
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

    @Override
    public Object[] findArguments(ParametersContainer parametersContainer) {
        Object[] result = new Object[parametersContainer.getParameterTypes().length];
        for (int i = 0; i < result.length; i++) {
            Parameter parameter = new Parameter(parametersContainer, i);
            result[i] = resolve(parameter);
        }
        return result;
    }

    private Object resolve(Parameter parameter) {
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

        properties.annotated(parameter.getParameterAnnotations());
        customizeProperties(parameter, properties);
        ResolvedBeanData extractedBean = getBeanExtractor().getBean(properties);
        return extractedBean.isResolved() ? extractedBean.getBeanValue() : null;
    }

    protected void customizeProperties(Parameter parameter, ResolvingPropertyDefinition properties) { }

    protected BeanExtractor getBeanExtractor() {
        return beanExtractor;
    }
}
