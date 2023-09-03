package ru.jengine.beancontainer.containercontext;

import ru.jengine.utils.serviceclasses.Stoppable;

public interface ContainerContext extends BeanExtractor, Stoppable {
    void constructBeans();
    void postConstructBeans();
}
