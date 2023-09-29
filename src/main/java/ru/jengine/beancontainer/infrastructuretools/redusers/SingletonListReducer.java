package ru.jengine.beancontainer.infrastructuretools.redusers;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesReducer;

import java.util.List;

@Bean(isInfrastructure = true)
public class SingletonListReducer implements BeanCandidatesReducer {
    @Override
    public int getReducerPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public ResolvedBeanData reduce(List<ResolvedBeanData> candidates) {
        return candidates.size() == 1 ? candidates.get(0) : ResolvedBeanData.NOT_RESOLVED;
    }
}
