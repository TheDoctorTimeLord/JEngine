package ru.jengine.beancontainer.beandefinitions;

import ru.jengine.beancontainer.utils.ParametersContainer;
import ru.jengine.beancontainer.exceptions.ContainerException;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BeanDefinition {
    Class<?> getBeanClass();
    String getScopeName();
    boolean isShared();
    @Nullable
    BeanProducer getBeanProducer();

    record BeanProducer(ParametersContainer parametersContainer, Function<Object[], Object> producer) {
        public BeanProducer(Supplier<Object> producer) {
            this(new ParametersContainer(new Class<?>[0], new Type[0], new Annotation[0][0], new Annotation[0]), args -> producer.get());
        }

        public BeanProducer(Method executable, Object executableOwner) {
            this(new ParametersContainer(executable), args -> {
                try {
                    return executable.invoke(executableOwner, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ContainerException("Error when executing method [%s] on [%s]".formatted(executable, executableOwner), e);
                }
            });
        }
    }
}
