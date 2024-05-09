package ru.jengine.beancontainer.infrastructuretools.transformers;

import ru.jengine.beancontainer.Constants.Extensions.TransformerPriorities;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.ClassesWith;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.contextmetainfo.ContextMetainfoManager;
import ru.jengine.beancontainer.contextmetainfo.metainfos.ModuleBasedContainerMetainfo;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanCandidatesTransformer;
import ru.jengine.beancontainer.extentions.infrastrucure.ContainerStateProvidable;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.utils.AnnotationUtils;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Bean(isInfrastructure = true)
public class ClassExtractorTransformer implements BeanCandidatesTransformer, ContainerStateProvidable {
    private ContextMetainfoManager contextMetainfoManager;

    @Override
    public void provide(ContainerState containerState) {
        this.contextMetainfoManager = containerState.getContextMetainfoManager();
    }

    @Override
    public int getTransformerPriority() {
        return TransformerPriorities.CLASS_EXTRACTOR;
    }

    @Override
    public List<ResolvedBeanData> transform(ResolvingProperties properties, List<ResolvedBeanData> candidates) {
        ClassesWith classesWith = AnnotationUtils.extractAnnotation(properties.getAnnotations(), ClassesWith.class);
        if (classesWith == null) {
            return candidates;
        }

        if (!ReflectionContainerUtils.isAvailableCollection(properties.getCollectionClass())) {
            throw new ContainerException("Annotation ClassesWith can be used with [%s] collections only"
                    .formatted(ReflectionContainerUtils.getAvailableCollectionAsString()));
        }

        if (!Class.class.equals(properties.getRequestedClass())) {
            throw new ContainerException("Collection annotated ClassesWith must contains java.lang.Class only");
        }

        if (contextMetainfoManager == null) {
            return List.of();
        }

        Class<? extends Annotation> targetAnnotation = classesWith.value();

        Stream<? extends Class<?>> candidatesClasses = candidates.stream()
                .map(candidate -> candidate.isResolved() ? candidate.getBeanValue() : null)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .filter(clazz -> AnnotationUtils.isAnnotationPresent(clazz, targetAnnotation));

        Stream<Class<?>> findByScanning = contextMetainfoManager.getRegisteredMetainfos().stream()
                .map(metainfo -> metainfo instanceof ModuleBasedContainerMetainfo moduleBased ? moduleBased : null)
                .filter(Objects::nonNull)
                .flatMap(metainfo -> metainfo.getModules().stream())
                .map(module -> module instanceof AnnotationModule annotationModule ? annotationModule : null)
                .filter(Objects::nonNull)
                .flatMap(module -> module.findAnnotatedClasses(targetAnnotation).stream());

        return Stream.concat(candidatesClasses, findByScanning)
                .distinct()
                .map(clazz -> new ResolvedBeanData(clazz, clazz))
                .toList();
    }
}
