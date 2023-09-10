package ru.jengine.beancontainer.containercontext.contexts;

import ru.jengine.beancontainer.beandefinitions.BeanDefinition;
import ru.jengine.beancontainer.beanfactory.BeanFactory;
import ru.jengine.beancontainer.containercontext.*;
import ru.jengine.beancontainer.exceptions.ContainerException;
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
    public Object getBean(ResolvingProperties properties) {
        ResolvingProperties[] aliased = classAliasManager.getForAlias(properties);
        if (aliased != null && aliased.length != 0) {
            //TODO добавить механизм адекватного резолва с учётом дополнительных средств
            Class<?> collectionClass = properties.getCollectionClass();
            if (collectionClass != null) {
                return beanResolver.resolveBeansAsCollection(scopes, aliased, collectionClass);
            }

            if (aliased.length != 1) {
                throw new ContainerException("Too many candidates for [%s]. Candidates: %s"
                        .formatted(properties.getRequestedClass(), aliased));
            }
            properties = aliased[0];
        }

        return beanResolver.resolveBeansMayBeCollection(scopes, properties);
    }
}
