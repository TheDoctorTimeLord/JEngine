package ru.jengine.beancontainer.infrastructuretools;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.infrastructuretools.redusers.SingletonListReducer;

import java.util.List;

public class StubBeanCandidatesService implements BeanCandidatesService {
    private final SingletonListReducer singletonListReducer = new SingletonListReducer();

    @Override
    public List<ResolvedBeanData> transformCandidates(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        return candidates;
    }

    @Override
    public ResolvedBeanData reduceCandidates(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        return singletonListReducer.reduce(properties, candidates);
    }
}
