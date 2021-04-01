package ru.jengine.beancontainer.implementation.infrastructure;

import org.reflections.ReflectionUtils;
import ru.jengine.beancontainer.BeanDefinition;
import ru.jengine.beancontainer.BeanPostProcessor;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.ContextPreProcessor;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Configurator;
import ru.jengine.beancontainer.annotations.Configurators;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.utils.BeanUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@Bean(isInfrastructure = true)
public class ConfiguratorsHandler implements ContextPreProcessor, BeanPostProcessor {
    private final Map<Class<?>, List<BeanConfigurator>> configurators = new HashMap<>();

    @Override
    public void preProcess(BeanDefinition definition) {
        if (definition.getBeanClass().isAnnotationPresent(Configurators.class)) {
            definition.setMustRemovedAfterPreProcess(true);

            Class<?> configurationsClass = definition.getBeanClass();
            Object configurationOwner = BeanUtils.createObjectWithDefaultConstructor(configurationsClass);
            ReflectionUtils.getMethods(configurationsClass).stream()
                    .filter(method -> method.isAnnotationPresent(Configurator.class))
                    .forEach(method -> {
                        Class<?>[] parameters = method.getParameterTypes();
                        if (parameters.length == 0) {
                            throw new ContainerException("Configurator [" + method + "] must have more then 0 parameters");
                        }
                        Class<?> configurableClass = parameters[0];

                        if (!configurators.containsKey(configurableClass)) {
                            configurators.put(configurableClass, new ArrayList<>());
                        }
                        configurators.get(configurableClass).add(new BeanConfigurator(configurationOwner, method));
                    });
        }
    }

    @Override
    public void postProcess(BeanContext bean, ContainerContext context) {
        if (configurators.containsKey(bean.getBeanClass())) {
            List<BeanConfigurator> beanConfigurators = configurators.get(bean.getBeanClass());
            beanConfigurators.forEach(configurator -> configurator.configure(bean.getBean(), context));
        }
    }

    private static class BeanConfigurator {
        private final MethodMeta configurator;
        private final List<Class<?>> neededParameters;

        public BeanConfigurator(Object configuratorOwner, Method configurationMethod) {
            this.configurator = new MethodMeta(configurationMethod, configuratorOwner);
            Class<?>[] parameters = configurationMethod.getParameterTypes();

            neededParameters = parameters.length > 1
                    ? Arrays.asList(Arrays.copyOfRange(parameters, 1, parameters.length))
                    : Collections.emptyList();
        }

        public void configure(Object configurable, ContainerContext context) {
            Object[] parameters = Stream.concat(Stream.of(configurable), neededParameters.stream()
                    .map(context::getBean)
                    .map(BeanContext::getBean))
                    .toArray();

            configurator.invoke(parameters);
        }
    }
}
