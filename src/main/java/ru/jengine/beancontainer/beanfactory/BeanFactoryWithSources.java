package ru.jengine.beancontainer.beanfactory;

import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingPropertyDefinition;
import ru.jengine.beancontainer.utils.Parameter;

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
}
