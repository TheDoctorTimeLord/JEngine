package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.*;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.infrastrucure.BeanProcessor;
import ru.jengine.utils.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class ScopableContainerContext implements ContainerContext {
    private final BeanResolver beanResolver;
    private final BeanCreationScope[] scopes;
    private final ClassAliasManager classAliasManager = new ClassAliasManager();

    public ScopableContainerContext(BeanFactory beanFactory, BeanResolver beanResolver,
            List<BeanDefinition> beanDefinitions, BeanCreationScopeResolver scopeResolver,
            List<BeanProcessor> beanProcessors, List<BeanPreRemoveProcessor> preRemoveProcessors)
    {
        this.beanResolver = beanResolver;

        this.scopes = CollectionUtils.groupBy(beanDefinitions, BeanDefinition::getScopeName)
                .entrySet()
                .stream()
                .map(e -> scopeResolver.resolve(e.getKey(), e.getValue(), beanFactory, this, beanProcessors, preRemoveProcessors))
                .toArray(BeanCreationScope[]::new);

        Arrays.stream(this.scopes)
                .flatMap(scope -> scope.getBeanClasses().stream())
                .forEach(classAliasManager::registerAliases);
    }

    @Override
    public void constructBeans() {
        for (BeanCreationScope scope : scopes) {
            scope.prepareStart();
        }
    }

    @Override
    public void postConstructBeans() {
        for (BeanCreationScope scope : scopes) {
            scope.postProcess();
        }
    }

    @Override
    public void stop() {
        for (BeanCreationScope scope : scopes) {
            scope.prepareStop();
        }
    }

    @Override
    public ResolvedBeanData getBean(ResolvingProperties properties) {
        ResolvingProperties[] propertiesOfAliases = classAliasManager.getForAlias(properties);
        ResolvingProperties[] propertiesForResolve = aggregateResolvingProperties(properties, propertiesOfAliases);

        List<ResolvedBeanData> resolvedBeans = beanResolver.resolveBeans(scopes, propertiesForResolve);
        if (resolvedBeans.isEmpty()) {
            return ResolvedBeanData.NOT_RESOLVED;
        }
        if (resolvedBeans.size() == 1) {
            return resolvedBeans.get(0);
        }
        return new ResolvedBeanData(resolvedBeans, List.class, true);
    }

    private ResolvingProperties[] aggregateResolvingProperties(ResolvingProperties initial, ResolvingProperties[]... aggregating) {
        int propertiesCount = 0;
        for (ResolvingProperties[] properties : aggregating) {
            propertiesCount += properties.length;
        }

        if (propertiesCount == 0) {
            return new ResolvingProperties[] { initial };
        }

        ResolvingProperties[] aggregated = new ResolvingProperties[propertiesCount];
        int currentPosition = 0;
        for (ResolvingProperties[] properties : aggregating) {
            int propertiesLength = properties.length;
            System.arraycopy(properties, 0, aggregated, currentPosition, propertiesLength);
            currentPosition += propertiesLength;
        }
        return aggregated;
    }
}
