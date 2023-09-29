package ru.jengine.beancontainer.operations.defaultimpl;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.ContainerState;
import ru.jengine.beancontainer.containercontext.ResolvedBeanData;
import ru.jengine.beancontainer.containercontext.contexts.ContainerContextFacade;
import ru.jengine.beancontainer.containercontext.resolvingproperties.ResolvingProperties;
import ru.jengine.beancontainer.exceptions.ContainerException;
import ru.jengine.beancontainer.infrastructuretools.BeanCandidatesService;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;

public class PresetContextFacadeOperation extends ContainerOperation {
    @Override
    public void apply(OperationResult previouseOperationResult, ContainerState state) {
        ContainerContextFacade contextFacade = state.getContainerContextFacade();

        ResolvedBeanData candidate = contextFacade.getBean(ResolvingProperties.properties(BeanCandidatesService.class)
                .beanContextSource(Constants.Contexts.INFRASTRUCTURE_CONTEXT));
        if (!candidate.isResolved()) {
            throw new ContainerException("BeanCandidatesService is not resolved");
        }
        if (candidate.isMultipleBean()) {
            throw new ContainerException("To many candidates for BeanCandidatesService. Candidates: " + candidate.getBeanValue());
        }
        BeanCandidatesService candidatesService = (BeanCandidatesService) candidate.getBeanValue();

        contextFacade.setBeanCandidatesService(candidatesService);
    }
}
