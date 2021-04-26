package ru.jengine.beancontainer.implementation.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

import org.reflections.ReflectionUtils;

import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.utils.AutowireUtils;
import ru.jengine.beancontainer.utils.BeanUtils;

public class AutowireBeanFactory implements BeanFactory {
    private final ContainerContext context;

    public AutowireBeanFactory(ContainerContext context) {
        this.context = context;
    }

    @Override
    public BeanContext buildBean(Class<?> beanClass) {
        Object bean = createObject(beanClass);
        BeanContext beanContext = new BeanContext(bean);

        postConstruct(beanClass, beanContext);

        return beanContext;
    }

    private void postConstruct(Class<?> beanClass, BeanContext beanContext) {
        for (Method method : ReflectionUtils.getMethods(beanClass)) {
            if (!method.isAnnotationPresent(PostConstruct.class)) {
                continue;
            }

            BeanUtils.invokePostConstructBeanMethod(new MethodMeta(method, beanContext.getBean()));
        }
    }

    private Object createObject(Class<?> beanClass) {
        Constructor<?> availableConstructor = BeanUtils.findAppropriateConstructor(beanClass);
        Object[] args = findArgs(availableConstructor);
        return BeanUtils.createObject(availableConstructor, args);
    }

    private Object[] findArgs(Executable parameterOwner) {
        Object[] result = new Object[parameterOwner.getParameterTypes().length];
        for (int i = 0; i < result.length; i++) {
            MethodParameter methodParameter = new MethodParameter(parameterOwner, i);
            result[i] = AutowireUtils.autowire(methodParameter, getContext());
        }
        return result;
    }

    protected ContainerContext getContext() {
        return context;
    }
}
