package ru.jengine.beancontainer.infrastructuretools;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;

import java.util.List;

public interface BeanCandidatesService {
    List<ResolvedBeanData> transformCandidates(List<ResolvedBeanData> candidates);
    ResolvedBeanData reduceCandidates(List<ResolvedBeanData> candidates);
}
