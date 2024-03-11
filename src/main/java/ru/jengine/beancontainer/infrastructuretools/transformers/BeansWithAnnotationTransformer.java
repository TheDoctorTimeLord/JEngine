package ru.jengine.beancontainer.infrastructuretools.transformers;

import ru.jengine.beancontainer.Constants.Extensions.TransformerPriorities;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.BeansWith;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesTransformer;
import ru.jengine.beancontainer.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.lang.annotation.Annotation;
import java.util.List;

@Bean(isInfrastructure = true)
public class BeansWithAnnotationTransformer implements BeanCandidatesTransformer {
    @Override
    public int getTransformerPriority() {
        return TransformerPriorities.BEANS_WITH_ANNOTATION;
    }

    @Override
    public List<ResolvedBeanData> transform(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        BeansWith beansWith = AnnotationUtils.extractAnnotation(properties.getAnnotations(), BeansWith.class);
        Class<? extends Annotation> targetAnnotation = beansWith == null ? properties.getExpectedAnnotation() : beansWith.value();
        if (targetAnnotation == null) {
            return candidates;
        }

        if (!ReflectionContainerUtils.isAvailableCollection(properties.getCollectionClass())) {
            throw new ContainerException("Annotation 'BeansWith' or parameter 'expectedAnnotation' can be used with [%s] collections only"
                    .formatted(ReflectionContainerUtils.getAvailableCollectionAsString()));
        }

        return candidates.stream()
                .filter(candidate -> AnnotationUtils.isAnnotationPresent(candidate.getBeanBaseClass(), targetAnnotation))
                .toList();
    }
}
