package ru.jengine.beancontainer2.containercontext.contexts;

import ru.jengine.beancontainer2.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer2.beanfactory.BeanFactory;
import ru.jengine.beancontainer2.containercontext.*;
import ru.jengine.beancontainer2.extentions.BeanCreationScopeResolver;
import ru.jengine.beancontainer2.extentions.BeanPreRemoveProcessor;
import ru.jengine.beancontainer2.extentions.BeanProcessor;
import ru.jengine.utils.CollectionUtils;

import java.util.List;

public class ScopableContainerContext implements ContainerContext {
    private final BeanResolver beanResolver;
    private final List<BeanCreationScope> scopes;
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
                .toList();

        this.scopes.stream()
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
    public Object getBean(ResolvingProperties properties) {
        ResolvingPropertyDefinition[] aliased = classAliasManager.getForAlias(properties);
        if (aliased != null && aliased.length != 0) {
            return beanResolver.resolveBeansAsCollection(scopes, aliased, properties.getCollectionClass());
        }

        return beanResolver.resolveBeansMayBeCollection(scopes, properties);
    }
}
