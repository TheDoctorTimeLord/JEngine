package ru.jengine.beancontainer.operations.defaultimpl;

import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.extentions.infrastrucure.ContainerStateProvidable;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;

import java.util.Collection;
import java.util.List;

public class ContainerStateToInfrastructureProvider extends ContainerOperation {
    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        Collection<ContainerStateProvidable> containerStateProvidingCandidates = state.getContainerContextFacade()
                .getBean(ResolvingProperties.properties(ContainerStateProvidable.class)
                        .beanContextSource(Contexts.INFRASTRUCTURE_CONTEXT)
                        .collectionClass(List.class))
                .asMultipleBeans(ContainerStateProvidable.class);

        for (ContainerStateProvidable containerStateProvidingCandidate : containerStateProvidingCandidates) {
            containerStateProvidingCandidate.provide(state);
        }
    }
}
