package ru.jengine.beancontainer.infrastructuretools;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.infrastructuretools.redusers.SingletonListReducer;

import java.util.List;

public class StubBeanCandidatesService implements BeanCandidatesService {
    private final SingletonListReducer singletonListReducer = new SingletonListReducer();

    @Override
    public List<ResolvedBeanData> transformCandidates(List<ResolvedBeanData> candidates) {
        return candidates;
    }

    @Override
    public ResolvedBeanData reduceCandidates(List<ResolvedBeanData> candidates) {
        return singletonListReducer.reduce(candidates);
    }
}
