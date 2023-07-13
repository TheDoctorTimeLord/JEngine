package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;

import java.util.List;

public interface BeanCandidatesTransformer {
    int getTransformerPriority();
    List<ResolvedBeanData> transform(List<ResolvedBeanData> candidates);
}
