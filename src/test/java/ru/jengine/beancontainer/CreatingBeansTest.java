package ru.jengine.beancontainer;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.intstructure.pac_10.R;
import ru.jengine.beancontainer.intstructure.pac_10.StartModuleWithPostConstructAndPreDestroy;
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
import ru.jengine.beancontainer.modules.AnnotationModule;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

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

    @ContainerModule(contextName = Constants.Contexts.DEFAULT_CONTEXT)
    private static class EmptyModule extends AnnotationModule { }
}
