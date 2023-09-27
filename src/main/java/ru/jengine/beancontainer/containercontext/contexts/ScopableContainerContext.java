package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.*;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer.extentions.BeanProcessor;
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
        ResolvingProperties[] propertiesForResolve = classAliasManager.getForAlias(properties);
        if (propertiesForResolve == null || propertiesForResolve.length == 0) {
            propertiesForResolve = new ResolvingProperties[] { properties };
        }

        List<ResolvedBeanData> resolvedBeans = beanResolver.resolveBeans(scopes, propertiesForResolve);
        if (resolvedBeans.isEmpty()) {
            return ResolvedBeanData.NOT_RESOLVED;
        }
        if (resolvedBeans.size() == 1) {
            return resolvedBeans.get(0);
        }
        return new ResolvedBeanData(resolvedBeans, List.class, true);
    }
}
