package ru.jengine.beancontainer.extentions.infrastrucure;

import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;

import java.util.List;

public interface BeanCandidatesTransformer {
    int getTransformerPriority();
    List<ResolvedBeanData> transform(ResolvingProperties properties, List<ResolvedBeanData> candidates);
}
