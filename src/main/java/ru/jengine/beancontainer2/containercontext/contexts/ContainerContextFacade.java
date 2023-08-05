package ru.jengine.beancontainer2.containercontext.contexts;

import ru.jengine.beancontainer2.containercontext.BeanExtractor;
import ru.jengine.beancontainer2.containercontext.ContainerContext;
import ru.jengine.beancontainer2.containercontext.ResolvingProperties;
import ru.jengine.beancontainer2.containercontext.ResolvingPropertyDefinition;
import ru.jengine.beancontainer2.exceptions.ContainerException;
import ru.jengine.beancontainer2.utils.BeanUtils;
import ru.jengine.utils.serviceclasses.Stoppable;

import javax.annotation.Nullable;
import java.util.*;

public class ContainerContextFacade implements BeanExtractor, Stoppable {
    private final Map<String, ContainerContext> containedContexts = new HashMap<>();

    public void registerContext(String contextName, ContainerContext context) {
        if (containedContexts.containsKey(contextName)) {
            throw new ContainerException("Context with name [%s] has been registered already".formatted(contextName));
        }

        containedContexts.put(contextName, context);
    }

    @Override
    @Nullable
    public Object getBean(ResolvingPropertyDefinition properties) {
        return BeanUtils.resolveBeansMayBeCollection(
                getBeanSources(properties.getBeanContextSources()),
                properties
        );
    }

    private Collection<? extends BeanExtractor> getBeanSources(String[] beanContextSources) {
        if (beanContextSources == null) {
            return containedContexts.values();
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
}
