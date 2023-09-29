package ru.jengine.beancontainer.infrastructuretools.redusers;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesReducer;
import ru.jengine.beancontainer.utils.AnnotationUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Bean(isInfrastructure = true)
public class MinOrderReducer implements BeanCandidatesReducer {
    @Override
    public int getReducerPriority() {
        return 65536;
    }

    @Override
    public ResolvedBeanData reduce(List<ResolvedBeanData> candidates) { //TODO подумать на счёт действий руками без стрима
        return candidates.stream()
                .map(data -> Map.entry(getBeanPriority(data.getBeanBaseClass()), data))
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .findFirst()
                .get();
    }

    private int getBeanPriority(Class<?> baseBeanClass) {
        Order order = AnnotationUtils.getAnnotationSafe(baseBeanClass, Order.class);
        return order == null ? Order.DEFAULT_VALUE : order.value();
    }
}
