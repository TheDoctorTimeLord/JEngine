package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;

import java.util.List;

public interface BeanCandidatesReducer {
    int getReducerPriority();
    ResolvedBeanData reduce(List<ResolvedBeanData> candidates);
}
