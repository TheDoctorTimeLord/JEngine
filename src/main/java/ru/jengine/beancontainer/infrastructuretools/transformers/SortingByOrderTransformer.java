package ru.jengine.beancontainer.infrastructuretools.transformers;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesTransformer;
import ru.jengine.beancontainer.utils.AnnotationUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Bean(isInfrastructure = true)
public class SortingByOrderTransformer implements BeanCandidatesTransformer {
    @Override
    public int getTransformerPriority() {
        return 0;
    }

    @Override
    public List<ResolvedBeanData> transform(List<ResolvedBeanData> candidates) {
        return candidates.stream()
                .map(data -> Map.entry(getBeanPriority(data.getBeanBaseClass()), data))
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .toList();
    }

    private int getBeanPriority(Class<?> baseBeanClass) {
        Order order = AnnotationUtils.getAnnotationSafe(baseBeanClass, Order.class);
        return order == null ? Order.DEFAULT_VALUE : order.value();
    }
}
