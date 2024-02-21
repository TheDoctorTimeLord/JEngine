package ru.jengine.beancontainer.infrastructuretools.redusers;

import java.util.List;

import ru.jengine.beancontainer.Constants.Extensions.ReducerPriorities;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesReducer;

@Bean(isInfrastructure = true)
public class SingletonListReducer implements BeanCandidatesReducer {
    @Override
    public int getReducerPriority() {
        return ReducerPriorities.SINGLETON_LIST;
    }

    @Override
    public ResolvedBeanData reduce(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        return candidates.size() == 1 ? candidates.get(0) : ResolvedBeanData.NOT_RESOLVED;
    }
}
