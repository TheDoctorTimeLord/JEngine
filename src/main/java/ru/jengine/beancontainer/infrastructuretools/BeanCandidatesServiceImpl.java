package ru.jengine.beancontainer.infrastructuretools;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesReducer;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesTransformer;

import java.util.Comparator;
import java.util.List;

@Bean(isInfrastructure = true)
public class BeanCandidatesServiceImpl implements BeanCandidatesService {
    private final BeanCandidatesTransformer[] candidatesTransformers;
    private final BeanCandidatesReducer[] candidatesReducers;

    public BeanCandidatesServiceImpl(List<BeanCandidatesTransformer> candidatesTransformers,
            List<BeanCandidatesReducer> candidatesReducers)
    {
        this.candidatesTransformers = candidatesTransformers.stream()
                .sorted(Comparator.comparingInt(BeanCandidatesTransformer::getTransformerPriority))
                .toArray(BeanCandidatesTransformer[]::new);
        this.candidatesReducers = candidatesReducers.stream()
                .sorted(Comparator.comparingInt(BeanCandidatesReducer::getReducerPriority))
                .toArray(BeanCandidatesReducer[]::new);
    }

    @Override
    public List<ResolvedBeanData> transformCandidates(List<ResolvedBeanData> candidates) {
        for (BeanCandidatesTransformer transformer : candidatesTransformers) {
            candidates = transformer.transform(candidates);
        }
        return candidates;
    }

    @Override
    public ResolvedBeanData reduceCandidates(List<ResolvedBeanData> candidates) {
        for (BeanCandidatesReducer reducer : candidatesReducers) {
            ResolvedBeanData reduced = reducer.reduce(candidates);
            if (reduced != null && reduced.isResolved()) {
                return reduced;
            }
        }
        return ResolvedBeanData.NOT_RESOLVED;
    }
}
