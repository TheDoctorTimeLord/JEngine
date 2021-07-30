package ru.jengine.beancontainer;

import java.util.List;

public interface ContextPattern {
    ContainerContext buildContext(BeanFactory factory);
    List<String> getBeanSources();

    boolean needLoadOnContainerInitialize();
    boolean wasLoaded();
    void setLoaded(boolean isLoaded);
}
