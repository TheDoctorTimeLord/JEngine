package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;

import java.util.List;

public interface BeanCandidatesReducer {
    int getReducerPriority();
    ResolvedBeanData reduce(ResolvingProperties properties, List<ResolvedBeanData> candidates);
}
