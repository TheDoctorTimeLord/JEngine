package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.ResolvingPropertyDefinition;

public class BeanFactoryWithSources extends DefaultBeanFactory {
    private final String[] sources;

    public BeanFactoryWithSources(BeanExtractor beanExtractor, String[] sources) {
        super(beanExtractor);
        this.sources = sources;
    }

    @Override
    protected Object resolve(MethodParameter parameter, ResolvingPropertyDefinition properties) {
        properties.beanContextSource(sources);
        return super.resolve(parameter, properties);
    }
}
