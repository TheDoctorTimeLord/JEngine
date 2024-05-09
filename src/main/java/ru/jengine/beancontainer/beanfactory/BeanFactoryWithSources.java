package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.annotations.Api;
import ru.jengine.beancontainer.annotations.Api.ApiElement;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingPropertyDefinition;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.Parameter;

import javax.annotation.Nullable;

public class BeanFactoryWithSources extends DefaultBeanFactory {
    private final String[] sources;

    public BeanFactoryWithSources(BeanExtractor beanExtractor, String[] sources) {
        super(beanExtractor);
        this.sources = sources;
    }

    @Override
    protected void customizeProperties(Parameter parameter, ResolvingPropertyDefinition properties) {
        properties.beanContextSource(sources);
    }

    @Override
    protected Object handleResolvedBean(Parameter parameter, ResolvedBeanData bean) {
        try {
            return super.handleResolvedBean(parameter, bean);
        } catch (ContainerException e) {
            Api apiAnnotation = AnnotationUtils.extractAnnotation(parameter.getContainer().getContainerAnnotations(), Api.class);
            if (apiAnnotation != null) {
                String resolution = findAppropriateResolution(apiAnnotation.value(), parameter.getParameterPosition());
                if (resolution != null) {
                    throw new ContainerException(e.getMessage() + "\nResolution: " + resolution);
                }
            }
            throw e;
        }
    }

    @Nullable
    private static String findAppropriateResolution(ApiElement[] value, int parameterPosition) {
        for (ApiElement element : value) {
            if (element.index() == parameterPosition) {
                return element.message();
            }
        }
        return null;
    }
}
