package ru.jengine.beancontainer2.containercontext;

import ru.jengine.utils.serviceclasses.Stoppable;

public interface ContainerContext extends BeanExtractor, Stoppable {
    void constructBeans();
    void postConstructBeans();
}
