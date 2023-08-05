package ru.jengine.beancontainer2.containercontext;

import javax.annotation.Nullable;

public interface BeanExtractor {
    Object NOT_RESOLVED = new Object();

    @Nullable
    Object getBean(ResolvingPropertyDefinition properties);
}
