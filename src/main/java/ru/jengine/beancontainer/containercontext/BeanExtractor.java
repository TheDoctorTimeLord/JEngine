package ru.jengine.beancontainer.containercontext;

import javax.annotation.Nullable;

public interface BeanExtractor {
    Object NOT_RESOLVED = new Object();

    @Nullable
    Object getBean(ResolvingProperties properties);
}
