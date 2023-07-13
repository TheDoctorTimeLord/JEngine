package ru.jengine.beancontainer.containercontext;

import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;

public interface BeanExtractor {
    ResolvedBeanData getBean(ResolvingProperties properties);
}
