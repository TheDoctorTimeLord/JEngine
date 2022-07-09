package ru.jengine.beancontainer.implementation.infrastructure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
import ru.jengine.beancontainer.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.BeanUtils;

@Bean(isInfrastructure = true)
public class ConfiguratorsHandler implements ContextPreProcessor, BeanPostProcessor {
    private final Map<Class<?>, List<BeanConfigurator>> configurators = new HashMap<>();

    @Override
    public void preProcess(BeanDefinition definition) {
        if (AnnotationUtils.isAnnotationPresent(definition.getBeanClass(), Configurators.class)) {
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

                        BeanConfigurator beanConfigurator = new BeanConfigurator(configurationOwner, method);

                        if (!configurators.containsKey(configurableClass)) {
                            configurators.put(configurableClass, new ArrayList<>());
                        }
                        List<BeanConfigurator> beanConfigurators = configurators.get(configurableClass);
                        if (!beanConfigurators.contains(beanConfigurator)) {
                            beanConfigurators.add(beanConfigurator);
                        }
                    });
        }
    }

    @Override
    public void postProcess(BeanContext bean, ContainerContext context) {
        List<BeanConfigurator> beanConfigurators = configurators.get(bean.getBeanClass());
        if (beanConfigurators != null) {
            beanConfigurators.forEach(configurator -> configurator.configure(bean.getBean(), context));
        }
    }

    private static class BeanConfigurator {
        private final MethodMeta configurator;

        private final Class<?>[] neededParameters;

        public BeanConfigurator(Object configuratorOwner, Method configurationMethod) {
            this.configurator = new MethodMeta(configurationMethod, configuratorOwner);
            Class<?>[] parameters = configurationMethod.getParameterTypes();

            neededParameters = parameters.length > 1
                    ? Arrays.copyOfRange(parameters, 1, parameters.length)
                    : new Class[0];
        }
        public void configure(Object configurable, ContainerContext context) {
            Object[] parameters = Stream.concat(Stream.of(configurable), Arrays.stream(neededParameters)
                            .map(context::getBean)
                            .map(BeanUtils::getBean)
                    ).toArray();

            configurator.invoke(parameters);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof BeanConfigurator beanConfigurator)) {
                return false;
            }
            return configurator.getMethod().equals(beanConfigurator.configurator.getMethod());
        }

        @Override
        public int hashCode() {
            return configurator.getMethod().hashCode();
        }
    }
}
