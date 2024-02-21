package ru.jengine.beancontainer.infrastructuretools;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;

import java.util.List;

public interface BeanCandidatesService {
    List<ResolvedBeanData> transformCandidates(ResolvingProperties properties, List<ResolvedBeanData> candidates);
    ResolvedBeanData reduceCandidates(ResolvingProperties properties, List<ResolvedBeanData> candidates);
}
