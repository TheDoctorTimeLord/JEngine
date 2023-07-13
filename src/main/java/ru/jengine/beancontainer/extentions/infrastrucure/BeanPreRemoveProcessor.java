package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.containercontext.BeanData;

public interface BeanPreRemoveProcessor {
    void preRemoveProcess(BeanData bean);
}
