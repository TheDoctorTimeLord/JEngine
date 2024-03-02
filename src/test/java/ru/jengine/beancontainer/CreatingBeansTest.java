package ru.jengine.beancontainer;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.intstructure.pac4.A;
import ru.jengine.beancontainer.intstructure.pac4.B;
import ru.jengine.beancontainer.intstructure.pac4.C;
import ru.jengine.beancontainer.intstructure.pac4.StartModuleWithBeans;
import ru.jengine.beancontainer.intstructure.pac5.D;
import ru.jengine.beancontainer.intstructure.pac5.E;
import ru.jengine.beancontainer.intstructure.pac5.StartModuleWithProcessors;
import ru.jengine.beancontainer.intstructure.pac6.F;
import ru.jengine.beancontainer.intstructure.pac6.G;
import ru.jengine.beancontainer.intstructure.pac6.StartModuleWithImportBean;
import ru.jengine.beancontainer.intstructure.pac7.H;
import ru.jengine.beancontainer.intstructure.pac7.I;
import ru.jengine.beancontainer.intstructure.pac7.J;
import ru.jengine.beancontainer.intstructure.pac7.StartModuleWithExistingBeans;
import ru.jengine.beancontainer.intstructure.pac8.*;
import ru.jengine.beancontainer.intstructure.pac9.*;
import ru.jengine.beancontainer.intstructure.pac_10.R;
import ru.jengine.beancontainer.intstructure.pac_10.StartModuleWithPostConstructAndPreDestroy;
import ru.jengine.beancontainer.intstructure.pac_11.RemoveCounter;
import ru.jengine.beancontainer.intstructure.pac_11.StartModuleWithModuleHierarchy;
import ru.jengine.beancontainer.intstructure.pac_12.StartModuleWithAfterInitializeBeans;
import ru.jengine.beancontainer.intstructure.pac_12.T;
import ru.jengine.beancontainer.intstructure.pac_13.*;
import ru.jengine.beancontainer.intstructure.pac_14.StartModuleWithBeanProducer;
import ru.jengine.beancontainer.intstructure.pac_14.Z;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.beancontainer.operations.ContainerOperation;
import ru.jengine.beancontainer.operations.OperationResult;
import ru.jengine.beancontainer.operations.special.RemoveContextOperation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;

public class CreatingBeansTest {
    @Test
    public void testCreatingBeans() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithBeans.class).build());
        container.initializeContainerByDefault();

        A actualA = container.getBean(A.class);
        B actualB = container.getBean(B.class);
        C actualC = container.getBean(C.class);

        Assert.assertNotNull(actualA);
        Assert.assertNotNull(actualB);
        Assert.assertNotNull(actualC);

        Assert.assertSame(actualA, actualB.getA());
        Assert.assertSame(actualA, actualC.getA());
        Assert.assertSame(actualB.getA(), actualC.getA());
        Assert.assertSame(actualB, actualC.getB());
    }

    @Test
    public void testHandleBeanProcessors() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithProcessors.class).build());
        container.initializeContainerByDefault();

        D actualD = container.getBean(D.class);
        E actualE = container.getBean(E.class);

        Assert.assertTrue(actualD.isGlobalTest());
        Assert.assertTrue(actualE.isTest());
    }

    @Test
    public void testImportBeans() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithImportBean.class).build());
        container.initializeContainerByDefault();

        F actualF = container.getBean(F.class);
        G actualG = container.getBean(G.class);

        Assert.assertNotNull(actualF);
        Assert.assertNotNull(actualG);

        Assert.assertSame(actualF, actualG.getF());
    }

    @Test
    public void testExistingBeans() {
        H externalH = new H();
        I externalI = new I(externalH);

        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithExistingBeans.class)
                .addBeans(externalH)
                .addBeans(Constants.Contexts.DEFAULT_CONTEXT, externalI)
                .build());
        container.initializeContainerByDefault();

        H actualH = container.getBean(H.class);
        I actualI = container.getBean(Constants.Contexts.DEFAULT_CONTEXT, I.class);
        J actualJ = container.getBean(J.class);

        Assert.assertNotNull(actualH);
        Assert.assertNotNull(actualI);
        Assert.assertNotNull(actualJ);

        Assert.assertSame(externalH, actualH);
        Assert.assertSame(externalI, actualI);

        Assert.assertSame(externalH, actualJ.getH());
        Assert.assertSame(externalI, actualJ.getI());
    }

    @Test
    public void testImplementationOfInterface() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithImplementations.class).build());
        container.initializeContainerByDefault();

        K actualK = container.getBean(K.class);
        L actualL = container.getBean(L.class);
        M actualM = container.getBean(M.class);
        N actualN = container.getBean(N.class);

        List<Int> actualImplementations = container.getBean(Int.class, List.class);

        Assert.assertEquals(4, actualImplementations.size());

        MatcherAssert.assertThat(actualImplementations, hasItem(actualK));
        MatcherAssert.assertThat(actualImplementations, hasItem(actualL));
        MatcherAssert.assertThat(actualImplementations, hasItem(actualN));
        MatcherAssert.assertThat(actualImplementations, not(hasItem(actualM)));

        List<Class<?>> actualClasses = actualImplementations.stream()
                .map(Object::getClass)
                .collect(Collectors.toList());
        MatcherAssert.assertThat(actualClasses, hasItem(M.class));
    }

    @Test
    public void testNoImplementationsOfInterface() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(EmptyModule.class).build());
        container.initializeContainerByDefault();

        List<Int> actualImplementations = container.getBean(Int.class, List.class);

        Assert.assertNotNull(actualImplementations);
        Assert.assertEquals(0, actualImplementations.size());
    }

    @Test
    public void testOrderingElementsInList() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithOrdering.class).build());
        container.initializeContainerByDefault();

        List<Ordered> actualImplementations = container.getBean(Ordered.class, List.class);

        Assert.assertEquals(3, actualImplementations.size());
        Assert.assertSame(Q.class, actualImplementations.get(0).getClass());
        Assert.assertSame(P.class, actualImplementations.get(1).getClass());
        Assert.assertSame(O.class, actualImplementations.get(2).getClass());
    }

    @Test
    public void testGetDifferentCollection() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithOrdering.class).build());
        container.initializeContainerByDefault();

        try
        {
            List<Ordered> list = container.getBean(Ordered.class, List.class);
            Set<Ordered> set = container.getBean(Ordered.class, Set.class);
            Collection<Object> collection = container.getBean(Ordered.class, Collection.class);

            //Чтобы исключить оптимизации:
            boolean res = list.isEmpty() || set.isEmpty() || collection.isEmpty();
        }
        catch (ClassCastException e) {
            Assert.fail("Any collection has incorrect type. " + e.getMessage());
        }
    }

    @Test
    public void testGetCandidateWithMinimalOrder() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithOrdering.class).build());
        container.initializeContainerByDefault();

        Ordered actual = container.getBean(Ordered.class);

        Assert.assertNotNull(actual);
        Assert.assertSame(Q.class, actual.getClass());
    }

    @Test
    public void testRunPostConstructAndPreDestroy() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithPostConstructAndPreDestroy.class).build());
        container.initializeContainerByDefault();

        R actual = container.getBean(R.class);

        container.stop();

        Assert.assertEquals(3, actual.getSpecialMethodsCalledCounter());
    }

    @Test
    public void testRemoveContexts() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithModuleHierarchy.class).build());
        container.initializeContainerByDefault();

        RemoveCounter removeCounter = container.getBean(RemoveCounter.class);

        container.executeOperations(new RemoveContextOperation("module1"));

        List<String> expectedRemovedModules = List.of("module1", "module2", "module3");
        Assert.assertEquals(expectedRemovedModules, removeCounter.getRemovedContexts());
        container.executeOperations(new ContainerOperation() {
            @Override
            public void apply(OperationResult previouseOperationResult, ContainerState state) {
                for (String expectedRemovedModule : expectedRemovedModules) {
                    Assert.assertFalse("ContainerContext [%s] was not removed".formatted(expectedRemovedModule),
                            state.getContainerContextFacade().hasContext(expectedRemovedModule));
                }
            }
        });
    }

    @Test
    public void testRunAfterInitializeMethod() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithAfterInitializeBeans.class).build());
        container.initializeContainerByDefault();

        T actual = container.getBean(T.class);

        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.test());
    }

    @Test
    public void testAnnotationScanning() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithAnnotatedElements.class).build());
        container.initializeContainerByDefault();

        X actual = container.getBean(X.class);

        MatcherAssert.assertThat(actual.getAnnotatedClasses(), hasItems(U.class, V.class));
        MatcherAssert.assertThat(actual.getAnnotatedClasses(), not(hasItem(W.class)));
    }

    @Test
    public void testBeanProducer() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(StartModuleWithBeanProducer.class).build());
        container.initializeContainerByDefault();

        Z actual = container.getBean(Z.class);

        Assert.assertNotNull(actual.getY());
    }

    @ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
    private static class EmptyModule extends AnnotationModule { }
}
