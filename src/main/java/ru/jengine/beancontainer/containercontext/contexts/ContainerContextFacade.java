package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.BeanResolver;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.infrastructuretools.BeanCandidatesService;
import ru.jengine.beancontainer.infrastructuretools.StubBeanCandidatesService;
import ru.jengine.beancontainer.utils.ReflectionContainerUtils;
import ru.jengine.utils.serviceclasses.Stoppable;

import java.util.*;

public class ContainerContextFacade implements BeanExtractor, Stoppable {
    private volatile BeanExtractor[] infrastructureBeanExtractor;
    private final BeanResolver beanResolver = new BeanResolver();
    private final Map<String, ContainerContext> containedContexts = new HashMap<>();
    private BeanCandidatesService candidatesService = new StubBeanCandidatesService();

    public void setBeanCandidatesService(BeanCandidatesService beanCandidatesService) {
        this.candidatesService = beanCandidatesService;
    }

    public void registerContext(String contextName, ContainerContext context) {
        if (containedContexts.containsKey(contextName)) {
            throw new ContainerException("Context with name [%s] has been registered already".formatted(contextName));
        }

        containedContexts.put(contextName, context);
    }

    public boolean hasContext(String contextName) {
        return containedContexts.containsKey(contextName);
    }

    public void constructBeans(List<String> contextNames) {
        ContainerContext[] contexts = contextNames.stream()
                .map(containedContexts::get)
                .toArray(ContainerContext[]::new);

        for (ContainerContext context : contexts) {
            context.constructBeans();
        }

        //Отдельный цикл для разделения друг от друга фаз construct и postConstruct
        for (ContainerContext context : contexts) {
            context.postConstructBeans();
        }
    }

    @Override
    public ResolvedBeanData getBean(ResolvingProperties properties) {
        List<ResolvedBeanData> beans = beanResolver.resolveBeans(
                getBeanSources(properties.getBeanContextSources()),
                properties
        );

        beans = candidatesService.transformCandidates(beans);

        Class<?> collectionClass = properties.getCollectionClass();
        if (collectionClass != null) {
            Collection<Object> beansCollection = beans.stream()
                    .map(ResolvedBeanData::getBeanValue)
                    .collect(
                            () -> ReflectionContainerUtils.createCollection(collectionClass),
                            Collection::add,
                            Collection::addAll
                    );
            return new ResolvedBeanData(beansCollection, collectionClass, false);
        }

        if (beans.isEmpty()) {
            return ResolvedBeanData.NOT_RESOLVED;
        }

        ResolvedBeanData candidate = candidatesService.reduceCandidates(beans);
        if (!candidate.isResolved()) {
            throw new ContainerException("Too many candidates for [%s]. Candidates: %s"
                    .formatted(properties.getRequestedClass(), beans));
        }
        return candidate;
    }

    private BeanExtractor[] getBeanSources(String[] beanContextSources) {
        if (beanContextSources == null) {
            return containedContexts.values().toArray(BeanExtractor[]::new);
        }

        if (beanContextSources.length == 1 && Constants.Contexts.INFRASTRUCTURE_CONTEXT.equals(beanContextSources[0])) {
            return getInfrastructureBeanExtractor();
        }

        return Arrays.stream(beanContextSources)
                .map(beanContextSource -> {
                    ContainerContext containerContext = containedContexts.get(beanContextSource);
                    if (containerContext == null) {
                        throw new ContainerException("Context [%s] is not found".formatted(beanContextSource));
                    }
                    return containerContext;
                })
                .toArray(BeanExtractor[]::new);
    }

    @Override
    public void stop() {
        Set<ContainerContext> stoppedContexts = new HashSet<>();
        for (ContainerContext context : containedContexts.values()) {
            if (!stoppedContexts.contains(context)) {
                context.stop();
                stoppedContexts.add(context);
            }
        }
    }

    private BeanExtractor[] getInfrastructureBeanExtractor() {
        if (infrastructureBeanExtractor == null) {
            synchronized (this) {
                if (infrastructureBeanExtractor == null) {
                    ContainerContext context = containedContexts.get(Constants.Contexts.INFRASTRUCTURE_CONTEXT);
                    if (context == null) {
                        throw new ContainerException("Context [%s] is not found".formatted(Constants.Contexts.INFRASTRUCTURE_CONTEXT));
                    }

                    infrastructureBeanExtractor = new BeanExtractor[] {context};
                }
            }
        }
        return infrastructureBeanExtractor;
    }
}
