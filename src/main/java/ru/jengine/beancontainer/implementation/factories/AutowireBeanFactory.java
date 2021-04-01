package ru.jengine.beancontainer.implementation.factories;

import org.reflections.ReflectionUtils;
import ru.jengine.beancontainer.BeanFactory;
import ru.jengine.beancontainer.ContainerContext;
import ru.jengine.beancontainer.annotations.PostConstruct;
import ru.jengine.beancontainer.dataclasses.BeanContext;
import ru.jengine.beancontainer.dataclasses.MethodMeta;
import ru.jengine.beancontainer.utils.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
        Object[] args = findArgs(availableConstructor.getParameterTypes());
        return BeanUtils.createObject(availableConstructor, args);
    }

    private Object[] findArgs(Class<?>[] parameterTypes) {
        Object[] result = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            result[i] = context.getBean(parameterTypes[i]).getBean();
        }
        return result;
    }

    protected ContainerContext getContext() {
        return context;
    }
}
