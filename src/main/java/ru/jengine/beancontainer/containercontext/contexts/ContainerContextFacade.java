package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.containercontext.BeanExtractor;
import ru.jengine.beancontainer.containercontext.BeanResolver;
import ru.jengine.beancontainer.containercontext.ContainerContext;
import ru.jengine.beancontainer.containercontext.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.utils.serviceclasses.Stoppable;

import javax.annotation.Nullable;
import java.util.*;

public class ContainerContextFacade implements BeanExtractor, Stoppable {
    private volatile List<BeanExtractor> infrastructureBeanExtractor;
    private final BeanResolver beanResolver = new BeanResolver(); //TODO подумать над инициализацией
    private final Map<String, ContainerContext> containedContexts = new HashMap<>();

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
        for (String contextName : contextNames) {
            ContainerContext context = containedContexts.get(contextName);
            context.constructBeans();
        }

        //Отдельный цикл для разделения друг от друга фаз construct и postConstruct
        for (String contextName : contextNames) {
            ContainerContext context = containedContexts.get(contextName);
            context.postConstructBeans();
        }
    }

    @Override
    @Nullable
    public Object getBean(ResolvingProperties properties) {
        return beanResolver.resolveBeansMayBeCollection(
                getBeanSources(properties.getBeanContextSources()),
                properties
        );
    }

    private Collection<? extends BeanExtractor> getBeanSources(String[] beanContextSources) {
        if (beanContextSources == null) {
            return containedContexts.values();
        }

        if (beanContextSources.length == 1 && Constants.Contexts.INFRASTRUCTURE_CONTEXT.equals(beanContextSources[0])) {
            return getInfrastructureBeanExtractor();
        }

        List<ContainerContext> beanSources = new ArrayList<>(beanContextSources.length);
        for (String beanContextSource : beanContextSources) {
            ContainerContext containerContext = containedContexts.get(beanContextSource);
            if (containerContext == null) {
                throw new ContainerException("Context [%s] is not found".formatted(beanContextSource));
            }
            beanSources.add(containerContext);
        }
        return beanSources;
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

    private List<BeanExtractor> getInfrastructureBeanExtractor() {
        if (infrastructureBeanExtractor == null) {
            synchronized (this) {
                if (infrastructureBeanExtractor == null) {
                    ContainerContext context = containedContexts.get(Constants.Contexts.INFRASTRUCTURE_CONTEXT);
                    if (context == null) {
                        throw new ContainerException("Context [%s] is not found".formatted(Constants.Contexts.INFRASTRUCTURE_CONTEXT));
                    }

                    infrastructureBeanExtractor = List.of(context);
                }
            }
        }
        return infrastructureBeanExtractor;
    }
}
