package ru.jengine.beancontainer;

import java.util.List;

public interface ContextPattern extends Cloneable {
    ContainerContext buildContext(BeanFactory factory);
    ContextPattern cloneWithContext(String newContextName);

    List<String> getBeanSources();
    boolean needLoadOnContainerInitialize();

    boolean wasLoaded();
    void setLoaded(boolean isLoaded);
}
