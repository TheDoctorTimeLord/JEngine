package ru.jengine.beancontainer.infrastructuretools.redusers;

import java.util.List;

import ru.jengine.beancontainer.Constants.Extensions.ReducerPriorities;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesReducer;

@Bean(isInfrastructure = true)
public class MinOrderReducer implements BeanCandidatesReducer {
    @Override
    public int getReducerPriority() {
        return ReducerPriorities.MIN_ORDER;
    }

    @Override
    public ResolvedBeanData reduce(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        // Так как трансформации найденных бинов происходят раньше, то и сортировка бинов по Order уже была ранее
        return candidates.getFirst();
    }
}
